package com.litmanovitziv.async_reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class AliveLogger {
    private Object locker = new Object();
    private int nActors = 0;
    private int bulkSize;

    private int counter;
    private int modCounter;

    private RandomAccessFile fileLogger;

    public AliveLogger(int bulkSize, String outputFile) {
        assert bulkSize > 0;
        this.bulkSize = bulkSize;

        try {
            fileLogger = new RandomAccessFile(outputFile, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void log(String message){
        try {
            fileLogger.writeBytes(String.format("%s %s\n", new Date().toString(), message));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addActors() {
    	nActors++;
    }

    public void increase() {
        Boolean shouldLog = false;
        int c;
        synchronized (locker) {
            counter++;
            c = counter;
            modCounter++;
            if (modCounter == bulkSize) {
                modCounter = 0;
                shouldLog = true;
            }
        }
        if (shouldLog) {
            try {
//                System.out.println(String.format("%s alive, so far %d processed\n", new Date().toString(), c));
                fileLogger.writeBytes(String.format("%s alive, so far %d entities was retrieved\n", new Date().toString(), c));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void reset() {
        this.counter = 0;
        this.modCounter = 0;
    }

    public synchronized void finish() {
    	nActors--;
    	if (nActors == 0) {
	        try {
	            fileLogger.writeBytes("done");
	            fileLogger.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    }
}
