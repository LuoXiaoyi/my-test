package com.jvm.core;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-16 18:02
 **/
public class SafepointTest {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            long l = 0;
            /*for (int i = 0; i < Integer.MAX_VALUE; i++) {
                for (int j = 0; j < Integer.MAX_VALUE; j++) {
                    if ((j & 1) == 1)
                        l++;
                }
            }*/
            while(true){
                ++l;
            }
            // System.out.println("How Odd:" + l);
        });
        t.setDaemon(true);
        t.start();
        long s = System.currentTimeMillis();
        Thread.sleep(5000);
        System.out.println("After " + (System.currentTimeMillis() - s) + " ms, it's done, system exit.");


        ThreadInfo[] threadInfos = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);
        for (ThreadInfo ti : threadInfos) {
            System.out.println(ti);
        }
    }
}
