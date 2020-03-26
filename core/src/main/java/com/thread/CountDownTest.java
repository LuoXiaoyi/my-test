package com.thread;

import java.util.concurrent.CountDownLatch;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-02 16:17
 **/
public class CountDownTest {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(System.getProperty("java.io.tmpdir"));


        System.out.println("test begin...");

        //new CountDownTest().test();

        System.out.println("test end...");
    }

    void test() throws InterruptedException {
        CountDownLatch cdLatch = new CountDownLatch(2);

        new Thread(new Task(cdLatch)).start();
        new Thread(new Task(cdLatch)).start();

        cdLatch.await();
    }

    private class Task implements Runnable {
        CountDownLatch cdl;

        public Task(CountDownLatch cdl) {
            this.cdl = cdl;
        }

        @Override
        public void run() {
            cdl.countDown();
            System.out.println(Thread.currentThread().getName() + "-> countDown...");
        }
    }
}
