package com.litmanovitziv.async_reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
