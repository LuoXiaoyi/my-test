package com.perfma.thread;

import com.perfma.thread.model.ThreadDumpMetadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nijiaben
 * @createTime 2018/5/23 09:40
 **/
public class Main {
    public static void main(String args[]) {
        File file = new File("/Users/xiaoyiluo/Desktop/deadlock");
        ThreadDumpParser parser = null;
        try {
            Map dumpMap = new HashMap();
            parser = DumpParserFactory.get().getDumpParserForLogfile(file, dumpMap, 1);
            ThreadDumpMetadata metadata = parser.parse();
            System.out.println("it's done...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
