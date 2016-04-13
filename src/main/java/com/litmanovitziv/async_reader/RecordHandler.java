package com.litmanovitziv.async_reader;

import java.util.concurrent.BlockingQueue;

public abstract class RecordHandler implements Runnable {
    protected BlockingQueue<String> _queue;

	public RecordHandler(BlockingQueue<String> queue) {
		this._queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				processLine(this._queue.take());
			} catch (Exception e) {
				done();
			}
		}
	}
	
	protected abstract void processLine(String line) throws Exception;
    protected abstract void done();
    
}
