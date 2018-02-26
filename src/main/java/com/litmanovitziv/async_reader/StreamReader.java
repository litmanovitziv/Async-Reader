package com.litmanovitziv.async_reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class StreamReader extends FileReader {

	public StreamReader(BlockingQueue<String> queue, String outputFile, int logBulkSize) {
		super(queue, outputFile, logBulkSize);
	}

	@Override
	public void run() {
       Arrays.stream(this._files)
       .map(file -> file.toPath())
       .forEach(file -> {
           try {
               processFile(file);
           } catch (IOException e) {
               e.printStackTrace();
           }
       });

       this.done();
	}

   /**
    *
    * @param file Path
    * @throws IOException
    */
   public void processFile(Path file) throws IOException {
       Files.lines(file)
               .parallel()
               .forEach(line -> {
                   try {
                	   _queue.put(line);
                       _logger.increase();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
   }
	
}
