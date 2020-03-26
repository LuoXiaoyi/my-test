package com.jvm.mem.oom;

import java.util.*;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-31 21:09
 **/
public class heapspace {

    /**
     * JVM 参数
     * -Xmx32m
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:HeapDumpPath=/Users/xiaoyiluo/Documents/my-space/code/test/core/src/main/java/com/jvm/mem/oom/
     * <p>
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * at java.util.HashMap.resize(HashMap.java:704)
     * at java.util.HashMap.putVal(HashMap.java:663)
     * at java.util.HashMap.put(HashMap.java:612)
     * at com.jvm.mem.oom.heapspace.main(heapspace.java:20)
     *
     * @param args
     */
    public static void main(String[] args) throws Exception{

        Thread.sleep(15000);
        final Map<Integer, Integer> map = new HashMap<>();
        Random r = new Random();

        Thread t = new Thread(() -> {
            System.out.println("child is running");
            try {
                while (true) {
                    map.put(new Integer(r.nextInt()), new Integer(r.nextInt()));
                }
            } catch (Throwable tw) {
                tw.printStackTrace();
                map.clear();
            }
        });

        t.start();
        t.join();
        Thread.sleep(20000);
        System.out.println("main begin to run...");
        final Map<Integer, Integer> map2 = new HashMap<>();
        while (true) {
            map2.put(new Integer(r.nextInt()), new Integer(r.nextInt()));
        }

    }
}
