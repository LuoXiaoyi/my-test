package com.jvm.mem.oom;

import java.util.Map;
import java.util.Random;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-31 18:38
 **/
public class GCLimitExceed {

    /**
     * With JVM boot parameters:
     *  0. -Xmx100m -XX:+UseParallelGC
     *  Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
     * 	at java.lang.Integer.valueOf(Integer.java:832)
     * 	at com.jvm.mem.oom.GCLimitExceed.main(GCLimitExceed.java:18)
     *
     *  1. -Xmx10m	-XX:+UseParallelGC
     *  Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 	at java.util.Hashtable.rehash(Hashtable.java:402)
     * 	at java.util.Hashtable.addEntry(Hashtable.java:426)
     * 	at java.util.Hashtable.put(Hashtable.java:477)
     * 	at com.jvm.mem.oom.GCLimitExceed.main(GCLimitExceed.java:38)
     *
     *  2. -Xmx100m -XX:+UseG1GC
     *  Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
     *
     *  3. -Xmx100m -XX:+UseConcMarkSweepGC
     *  Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
     *  *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message can't create byte arrau at JPLISAgent.c line: 813
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        Map map = System.getProperties();
        Random r = new Random();
        while (true) {
            map.put(r.nextInt(),"value");
        }
    }
}
