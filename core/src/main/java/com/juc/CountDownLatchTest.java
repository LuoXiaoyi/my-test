package com.juc;

import java.util.concurrent.CountDownLatch;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/6/8 22:12
 * @Version 1.0
 **/
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        System.out.println("in " + Thread.currentThread().getName() + "...");
        System.out.println("before latch.await()...");

        for (int i = 1; i <= 3; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    System.out.println("enter Thread " + getName() + "...");
                    System.out.println("execute countdown...");
                    latch.countDown();
                    System.out.println("exit Thread" + getName() + ".");
                }
            }.start();
        }
        Thread.sleep(10000);
        latch.await();
        System.out.println("in " + Thread.currentThread().getName() + "...");
        System.out.println("after latch.await()...");
    }
}
