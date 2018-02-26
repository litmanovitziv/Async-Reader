package com.litmanovitziv.async_reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ThreadedReader extends FileReader {
    private ExecutorService _executor;
    private int _nThreads;

	public ThreadedReader(BlockingQueue<String> queue, String outputFile, int nThreads, int logBulkSize) {
		super(queue, outputFile, logBulkSize);
        this._nThreads = nThreads;
        _executor = Executors.newFixedThreadPool(nThreads);
	}

	@Override
	public void run() {
        List<SyncLineReader> readers = Arrays.stream(this._files)
                .map(file -> {
                    try {
                        return new SyncLineReader(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(reader -> reader != null)
                .collect(Collectors.toList());

        SyncChainedReaders syncChainedReaders = new SyncChainedReaders(readers);

        CountDownLatch latch = new CountDownLatch(_nThreads);
        for (int k = 0; k < _nThreads; ++k) {
            _executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String line = syncChainedReaders.tryGetNextLine();
                            if (null == line)
                                break;

                            try {
                            	_queue.put(line);
                                _logger.increase();
                            } catch (Exception e) {
                                _logger.log(String.format("%s record couldn't been processed\t%s\n", new Date().toString(), e.getMessage()));
                            }
                        }

                        latch.countDown();
                    } catch (IOException e) {
                        _logger.log(String.format("%s %s\n", new Date().toString(), e.getMessage()));
                    }
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            _logger.log(String.format("%s %s\n", new Date().toString(), e.getMessage()));
        }
        _executor.shutdown();

        this.done();
    }
	
}
