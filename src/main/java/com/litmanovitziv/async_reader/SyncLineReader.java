package com.litmanovitziv.async_reader;

import java.io.*;

public class SyncLineReader {
    private BufferedReader br;

    public SyncLineReader(File file) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(file));
    }

    public synchronized String tryGetNextLine() throws IOException {
        String line = br.readLine();
        return line;
    }
}
