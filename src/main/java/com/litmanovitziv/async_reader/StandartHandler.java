package com.litmanovitziv.async_reader;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class StandartHandler extends RecordHandler {
    private RandomAccessFile _fileAsyncWriter;

	public StandartHandler(BlockingQueue<String> queue, String outputFile) throws FileNotFoundException {
		super(queue);
        _fileAsyncWriter = new RandomAccessFile(outputFile, "rw");
	}
	
	@Override
	public void processLine(String line) throws Exception {
		System.out.println(line);
		this.writeRecord(line);
	}

    private void writeRecord(String record) {
        // TODO : Adding lock on output file
        synchronized (_fileAsyncWriter) {
            try {
                String objPayload = record + System.getProperty("line.separator");
                byte[] bytes = objPayload.getBytes(StandardCharsets.UTF_8);
                _fileAsyncWriter.write(bytes);
            } catch (Exception e) {
                // TODO : log write exception
//                logger.log(String.format("%s record %s couldn't been written\t%s\n", new Date().toString(), record, e.getMessage()));
            }
        }
    }

    protected void done() {
        try {
            _fileAsyncWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
