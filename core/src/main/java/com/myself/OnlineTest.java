package com.myself;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-22 17:39
 **/
public class OnlineTest {
    static final int LIMIT = Integer.MAX_VALUE;

    public static void main(String[] args) throws Throwable {
        HashMapPutThread pt1 = new HashMapPutThread();
        HashMapPutThread pt2 = new HashMapPutThread();
        HashMapPutThread pt3 = new HashMapPutThread();
        HashMapPutThread pt4 = new HashMapPutThread();
        HashMapPutThread pt5 = new HashMapPutThread();

        pt1.start();
        pt2.start();
        pt3.start();
        pt4.start();
        pt5.start();

        Thread.currentThread().join();
        System.out.println("finished...");
    }

    static class HashMapPutThread extends Thread {
        private static AtomicInteger ai = new AtomicInteger(0);
        private static Map<Integer, Integer> map = new HashMap<Integer, Integer>(1);

        @Override
        public void run() {
            while (ai.get() < LIMIT) {
                map.put(ai.get(), ai.get());
                ai.incrementAndGet();
            }
        }
    }

}
