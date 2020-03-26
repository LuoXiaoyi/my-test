package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 02/06/2018 17:19
 **/
public class DoubleClockWaitTest {

    public static void main(String[] args) throws InterruptedException {
        Object lock1 = new Object(), lock2 = new Object();
        new Thread(new DoubleClockRunnable(lock1,lock2), "DoubleClockRunnable").start();
        Thread.sleep(10000);
        new Thread(new DoubleClockRunnable(lock1,lock2), "DoubleClockRunnable2").start();
    }

    private static class DoubleClockRunnable implements Runnable {
        DoubleClockRunnable(Object lock1, Object lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        private Object lock1, lock2;

        public void run() {
            System.out.println(Thread.currentThread().getName() + " entering run()... ");
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " got the lock: " + lock1);
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " got the lock: " + lock2);
                    try {
                        lock2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
