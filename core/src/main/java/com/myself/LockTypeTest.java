package com.myself;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiluo
 * @createTime 2018/12/26 16:44
 **/
public class LockTypeTest {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> test(), "class-synchronized").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test2();
        }, "object-synchronized").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test3();
        }, "ReentrantLock lock").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test4();
        }, "LockSupport.park\"(LockTypeTest\".class)").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test5();
        }, "LockSupport.park(obj)").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test6();
        }, "synchronized (obj)").start();

        Object obj = new LockTypeTest();
        Object obj2 = new Object();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test8(obj);
        }, "synchronized (obj)").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test7(obj);
        }, "synchronized (obj) - waiting").start();

        new Thread(() -> {
            LockTypeTest t = new LockTypeTest();
            t.test9(obj, obj2);
        }, "synchronized (obj) - waiting").start();

        test_juc_deadLock();
        test_monitor_deadLock();
        test_thread_pool();
        test_same_stack();

        synchronized (obj2) {
            obj2.notifyAll();
        }

        // executor.shutdown();
    }

    static class SameTask implements Runnable {
        public void run() {
            Random r = new Random();
            int a = r.nextInt(100);
            System.out.println(a);
            next();
        }

        private void next() {
            System.out.println("next");
            next2();
        }

        private void next2() {
            System.out.println("next2");
            next3();
        }

        private void next3() {
            System.out.println("next3");
            try {
                Thread.sleep(1000 * 1000);
            } catch (InterruptedException t) {
            }
        }
    }

    private static void test_same_stack() {
        new Thread(new SameTask(), "test_same_stack_1").start();
        new Thread(new SameTask(), "test_same_stack_2").start();
    }

    private static void test_thread_pool() {
        executor = new ThreadPoolExecutor(2, 4,
                0l, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(100), new ThreadFactory() {
            int i = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "test_thread_pool-" + (i++));
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; ++i) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is running...");
                        Thread.sleep(1000 * 1000);
                    } catch (InterruptedException ie) {
                    }
                }
            });
        }

        executor2 = new ThreadPoolExecutor(2, 4,
                0l, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100), new ThreadFactory() {
            int i = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "idle_thread_pool-" + (i++));
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; ++i) {
            executor2.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                }
            });
        }
    }

    private static void test_monitor_deadLock() {
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName());
                try {

                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " enter 2");
                }
            }
        }, "test_monitor_deadLock-1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock1) {
                        System.out.println(Thread.currentThread().getName() + " enter 2");
                    }
                }
            }
        }, "test_monitor_deadLock-2").start();
    }

    private static void test_juc_deadLock() {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        new Thread(() -> {
            lock1.lock();
            System.out.println(Thread.currentThread().getName());
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
        }, "test_juc_deadLock-1").start();

        new Thread(() -> {
            lock2.lock();
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
        }, "test_juc_deadLock-2").start();
    }

    private void test9(Object obj, Object obj2) {
        try {
            synchronized (obj2) {
                obj2.wait();
                synchronized (obj) {
                    obj.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello un parking...");

    }

    private void test8(Object obj) {
        try {
            synchronized (obj) {
                Thread.sleep(1000000000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello un parking...");
    }

    private void test7(Object obj) {
        try {
            synchronized (obj) {
                obj.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello un parking...");

    }

    private void test6() {
        Object obj = new LockTypeTest();
        try {
            synchronized (obj) {
                obj.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello un parking...");

    }

    private void test5() {
        Object obj = new LockTypeTest();
        LockSupport.park(obj);
        System.out.println("hello un parking...");

    }

    private void test4() {
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());

        LockSupport.park(LockTypeTest.class);
        System.out.println("park one...");
        LockSupport.park(LockTypeTest.class);
        System.out.println("park two...");

        System.out.println("hello un parking...");

    }

    private void test3() {
        Object o = new Object();
        try {
            lock.lock();
            synchronized (o) {
                o.wait();
            }
            // Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private synchronized void test2() {
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void test() {
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static ThreadPoolExecutor executor;
    static ThreadPoolExecutor executor2;
}
