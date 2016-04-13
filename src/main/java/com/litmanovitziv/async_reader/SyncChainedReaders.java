package com.litmanovitziv.async_reader;

import java.io.IOException;
import java.util.List;

public class SyncChainedReaders {
    private int currentIndex;
    private List<SyncLineReader> readers;

    public SyncChainedReaders(List<SyncLineReader> readers) {
        this.readers = readers;
        this.currentIndex = 0;
    }

    public synchronized String tryGetNextLine() throws IOException {
        while (currentIndex < readers.size()) {
            SyncLineReader lineReader = readers.get(currentIndex);
            String line = lineReader.tryGetNextLine();
            if (line != null) {
                return line;
            }
            currentIndex++;
        }

        return null;
    }
}
