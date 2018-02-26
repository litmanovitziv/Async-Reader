package com.litmanovitziv.async_reader;

import java.util.concurrent.BlockingQueue;

public abstract class RecordHandler implements Runnable {
    protected BlockingQueue<String> _queue;

	public RecordHandler(BlockingQueue<String> queue) {
		this._queue = queue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				processRecord(this._queue.take());
			}
//			done();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void processRecord(String line) throws Exception;
    protected abstract void done();
    
}
