package com.litmanovitziv.async_reader;

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {

    /**
     * @param args
     * Optional arguments
     * args[0]: input direcory
     * args[1]: input files names, regex. Such as ^Companies[1-9].json$
     * args[2]: output file name
     * args[3]: Number of Thread
     * args[4]: bulk test
     */
	public static void main(String[] args) {
		FileReader reader1 = null, reader2 = null;
		RecordHandler handler = null;
		BlockingQueue<String> queue = new LinkedBlockingDeque<String>();

        try {
            reader1 = new StreamReader(queue, "loger1.json", 1);
            reader1.load(".", "^stream1.json$");
            reader2 = new ThreadedReader(queue, "loger2.json", 1, 1);
            reader2.load(".", "^stream2.json$");
            handler = new StandartHandler(queue, "output.json");
            Thread producer1 = new Thread(reader1);
            Thread producer2 = new Thread(reader2);
            Thread concumer = new Thread(handler);
            producer1.start();
            producer2.start();
            concumer.start();
            producer1.join();
            producer2.join();
            concumer.join();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.exit(0);
	}

}
