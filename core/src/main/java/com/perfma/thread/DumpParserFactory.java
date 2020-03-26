package com.perfma.thread;

import com.perfma.thread.util.DateMatcher;

import java.io.*;
import java.util.Map;

public class DumpParserFactory {
    private static DumpParserFactory instance = null;

    private DumpParserFactory() {
    }

    public static DumpParserFactory get() {
        if (instance == null) {
            instance = new DumpParserFactory();
        }
        return instance;
    }

    @SuppressWarnings("rawtypes")
    public ThreadDumpParser getDumpParserForLogfile(File file, Map threadStore, int startCounter) {
        BufferedReader bis = null;
        int readAheadLimit = 6319200;//126384;
        int lineCounter = 0;
        ThreadDumpParser currentDumpParser = null;

        try {
            bis = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            DateMatcher dm = new DateMatcher();
            while (bis.ready() && (currentDumpParser == null)) {
                bis.mark(readAheadLimit);
                String line = bis.readLine();
                dm.checkForDateMatch(line);
                if (HotspotThreadDumpParser.checkForSupportedThreadDump(line)) {
                    currentDumpParser = new HotspotThreadDumpParser(file, bis, threadStore, lineCounter, startCounter, dm);
                }
                lineCounter++;
            }
            if ((currentDumpParser != null) && (bis != null)) {
                bis.reset();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return currentDumpParser;
    }
}
