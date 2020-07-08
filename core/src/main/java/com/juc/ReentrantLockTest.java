package com.juc;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/6/7 20:54
 * @Version 1.0
 **/
public class ReentrantLockTest {

    public static void main(String[] args) throws Exception {
        ReentrantLock lock = new ReentrantLock();

        lock.lock();
        System.out.println("hello");
        lock.unlock();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //while (true) {
                    LockSupport.park(this);
                    System.out.println("unparked");
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().isInterrupted());
                    System.out.println("finally");
                    System.out.println(Thread.interrupted());
                    Thread.currentThread().interrupt();
                    final Object a = new Object();
                    try {
                        synchronized (a) {
                            a.wait();
                            System.out.println("wait ok....");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        LockSupport.unpark(t);

        t.start();

        Thread.sleep(1000);
        System.out.println("begin to interrupt thread t");
        t.interrupt();
    }
}
