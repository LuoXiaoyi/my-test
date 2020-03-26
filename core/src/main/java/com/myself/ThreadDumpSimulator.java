package com.myself;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class ThreadDumpSimulator {


    public static void main(String[] args) throws InterruptedException {

        System.out.println("Default priority: " + Thread.currentThread().getPriority());

        Thread t1 = new Thread(new DeadLockTask(false), "t1");

        System.out.println("t1's priority: " + t1.getPriority());

        Thread t2 = new Thread(new DeadLockTask(true), "t2");

        t1.start();
        t2.start();

        Thread t3 = new Thread(new DeadLockTask2(false), "t3");
        Thread t4 = new Thread(new DeadLockTask2(true), "t4");

        t3.start();
        t4.start();

        // 模拟New状态的线程，不过失败了
        Thread newThread = new Thread("NEW-Thread");

        // 通过object.wait(max_value)来模拟等待
        Thread timedWaiting = new Thread(new TimedWaitingTask(true), "timedWait(max-value)");
        timedWaiting.start();

        Thread timedWaiting2 = new Thread(new TimedWaitingTask(false), "timedWait");
        timedWaiting2.start();

        Object lock = new Object();
        Thread sleeping1 = new Thread(new SleepingTask(lock), "sleeping1");
        sleeping1.start();

        Thread sleeping2 = new Thread(new SleepingTask(lock), "sleeping2");
        sleeping2.start();

        Thread timedPark = new Thread(new ParkTask(true, new Object()), "timedPark");
        timedPark.start();

        Thread parkThread = new Thread(new ParkTask(false, new Object()), "parkThread");
        parkThread.start();

        Object parkLock = new Object();
        Thread anonymousThread = new Thread(new ParkTask(false, parkLock));
        anonymousThread.start();

        System.out.println("Main thread going to sleep 1 second");
        Thread.sleep(1000);

        Thread anotherAnonymousThread = new Thread(new ParkTask(false, parkLock));
        anotherAnonymousThread.start();

        System.out.println("=========================================");

        Thread.sleep(10000);

        LockSupport.unpark(anonymousThread);
        System.out.println("  unpark : " + anonymousThread.getName());


        Thread.sleep(100000);


        /**
         * 线程池
         */
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(new ParkTask(true, new Object()));
        executor.execute(new SleepingTask(new Object()));
        executor.execute(new TimedWaitingTask(true));

        /**
         * 另外一个线程池
         */
        executor = Executors.newFixedThreadPool(4);
        executor.execute(new ParkTask(false, new Object()));
        executor.execute(new SleepingTask(new Object()));
        executor.execute(new TimedWaitingTask(false));

        /**
         * 单线程池
         */
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new SleepingTask(new Object()));

        Thread thread = new Thread(new ParkTask(false, new Object()));
        thread.start();

        Object lock3 = new Object();
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++ ");
        System.out.println("lock3: " + lock3);
        {
            synchronized (lock3) {
                for (int i = 0; i < 10000; ++i) {
                    try {
                        ForFinalizerThreadTest test = new ForFinalizerThreadTest(lock3);
                        //System.out.println(i + " ForFinalizerThreadTest...");
                        Thread.sleep(100);
                        test = null;
                        System.gc();
                    } catch (Exception e) {
                        System.out.println("catch: " + e.getMessage());
                        // e.printStackTrace();
                    }

                }

                Thread.sleep(1000 * 1000);
            }

        }

        System.out.println(" *******&&&&&&&&**********   done. ");
        // 手动GC，触发 Finalizer 线程执行 ForFinalizerThreadTest 的 finalize 方法
        System.gc();

    }

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    private static Object lock3 = new Object();
    private static Object lock4 = new Object();

    private static class DeadLockTask2 implements Runnable {

        private boolean flag = false;

        public DeadLockTask2(boolean f) {
            this.flag = f;
        }

        public void run() {
            while (true) {

                if (flag) {

                    synchronized (lock3) {

                        System.out.println(Thread.currentThread().getName() + " hold lock3");

                        synchronized (lock4) {
                            System.out.println(Thread.currentThread().getName() + " hold lock4");
                        }
                    }
                } else {
                    synchronized (lock4) {

                        System.out.println(Thread.currentThread().getName() + " hold lock4");

                        synchronized (lock3) {
                            System.out.println(Thread.currentThread().getName() + " hold lock3");
                        }
                    }
                }
            }
        }
    }

    private static class DeadLockTask implements Runnable {

        private boolean flag = false;

        public DeadLockTask(boolean f) {
            this.flag = f;
        }

        public void run() {
            while (true) {

                if (flag) {

                    synchronized (lock1) {

                        System.out.println(Thread.currentThread().getName() + " hold lock1");

                        synchronized (lock2) {
                            System.out.println(Thread.currentThread().getName() + " hold lock2");
                        }
                    }
                } else {
                    synchronized (lock2) {

                        System.out.println(Thread.currentThread().getName() + " hold lock2");

                        synchronized (lock1) {
                            System.out.println(Thread.currentThread().getName() + " hold lock1");
                        }
                    }
                }
            }
        }
    }
}
