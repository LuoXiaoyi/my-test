package com.perfma.thread;

import com.perfma.thread.model.ThreadDumpMetadata;

import java.io.IOException;

public interface ThreadDumpParser {

    ThreadDumpMetadata parse();

    void close() throws IOException;

}
