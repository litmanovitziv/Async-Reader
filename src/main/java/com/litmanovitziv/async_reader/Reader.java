package com.litmanovitziv.async_reader;

import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public abstract class Reader implements Runnable {
    protected File[] _files = {};
    protected BlockingQueue<String> _queue;
    protected RecordHandler _handler;
    protected AliveLogger _logger = new AliveLogger(1, "reading_logger");
    
    protected Reader(BlockingQueue<String> queue, String outputFile, int logBulkSize) {
        _logger = new AliveLogger(logBulkSize, outputFile);
//    	_logger.addActors();
        _queue = queue;
    }
    
    public void load(String dirPath, String identifier) {
        File dir = new File(dirPath);
        FileFilter fileFilter = new RegexFileFilter(identifier);
        this._files = dir.listFiles(fileFilter);
        Arrays.sort(this._files);
    }

    protected void done() {
        _logger.finish();
    }

}
