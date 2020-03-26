package com.thread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author xiluo
 * @createTime 2019/3/1 13:36
 **/
public class ThreadInterrupt {

    public static void main(String[] args) {
        Thread tt = new Thread(() -> {
            for (int i = 0; i < 1000; ++i) {
                queue.offer("abc: " + i);
            }
        });
        tt.start();

        try {
            tt.join();
            System.out.flush();
            System.out.println("offer thread is done. waiting 10s...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        running = false;
        t.interrupt();
        System.out.println("main thread is done...");
    }

    static volatile boolean running = true;

    static BlockingDeque<String> queue = new LinkedBlockingDeque<>();

    static Thread t = new Thread(() -> {
        while (running) {
            try {
                String s = queue.take();
                System.out.println(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("demon thread is done...");
    });

    static {
        // t.setDaemon(true);
        t.start();
    }
}
