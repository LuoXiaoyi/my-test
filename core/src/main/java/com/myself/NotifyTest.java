package com.myself;

/**
 * 线程 wait 的顺序，与线程被唤醒的顺序存在如下关系：
 * 1. 如果是 notify 单个线程，则是以 FIFO 的原则唤醒，即第一个被 wait 的线程首先被唤醒；
 * 2. 如果是 notifyAll 所有的线程，则是以 LIFO 的原则唤醒，即最后一个被 wait 住的线程首先被唤醒。
 *
 * 线程 yield
 * 让出 CPU 的执行时间片
 * 在 CPP 的解释中是这样对这个方法解释的
 * The exact behavior of this function depends on the implementation, in particular on the mechanics of the OS scheduler
 * in use and the state of the system. For example, a first-in-first-out realtime scheduler (SCHED_FIFO in Linux) would
 * suspend the current thread and put it on the back of the queue of the same-priority threads that are ready to run
 * (and if there are no other threads at the same priority, yield has no effect).
 * 就是说，虽然会让出 CPU 的执行权限，但是，实际情况可能和不同的 OS 的实时调度器相关。有些 FIFO 的调度器，会把当前 yield 的线程放到
 * 队列的末尾（注意这个队列是指同优先级别的队列，操纵系统的处理器在调度线程的时候，是根据线程的优先级做队列划分的，也就是说，对其他优先级的
 * 线程没啥影响），注意，当队列中有较多同优先级的线程时，当前线程可能会被停滞较长时间，如果队列中只有一个即当前线程，那 yield 对当前线程来说
 * 可能就没什么影响了。
 * @author xiaoyiluo
 * @createTime 2018/6/7 23:30
 **/
public class NotifyTest {

    private static Object pLock = new Object();

    public static void main(String[] args) {




        for(int i = 0; i < 100; ++i){
            new Thread(new Runnable() {
                public void run() {
                    while(true){
                        // do nothing....
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            public void run() {
                long start = System.nanoTime();
                long end = start;
                do {
                    // Thread.yield();  // 10000000545  or  10000000003
                    end = System.nanoTime();
                } while (end - start < 10000000000l);

                System.out.println("end - start: " + (end - start));

                // 10007121816   or   10124072068

            }
        }).start();

        while (true){
            //

            int a = 0;
        }

        /*Thread t1 = new Thread(new Runnable() {
            public void run() {

                synchronized (pLock) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " sleeping...");
                        Thread.sleep(10000);

                        System.out.println(Thread.currentThread().getName() + " finish");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                synchronized (pLock) {
                    System.out.println(Thread.currentThread().getName() + " yield");
                    Thread.yield();
                    System.out.println(Thread.currentThread().getName() + " after yield");


                }
            }
        });
        t2.start();*/


    }

    private static void test() {
        Object lock = new Object();
        Thread t1 = new Thread(new WaitTask(lock), "t1");
        Thread t2 = new Thread(new WaitTask(lock), "t2");
        Thread t3 = new Thread(new WaitTask(lock), "t3");
        Thread t4 = new Thread(new WaitTask(lock), "t4");
        Thread t5 = new Thread(new WaitTask(lock), "t5");

        t1.start();
        t2.start();
        t3.start();
        t5.start();
        t4.start();

        Thread n6 = new Thread(new NotifyTask(lock), "n6");
        Thread n7 = new Thread(new NotifyTask(lock), "n7");

        n6.start();
        n7.start();
    }


    public static class NotifyTask implements Runnable {

        Object lock;

        NotifyTask(Object lock) {

            this.lock = lock;
        }

        public void run() {

            synchronized (lock) {


                System.out.println(Thread.currentThread().getName() + " is going to notify all waiting thread.");

                lock.notify();

                System.out.println(Thread.currentThread().getName() + " after notified.");

            }

        }
    }


    public static class WaitTask implements Runnable {

        Object lock;

        WaitTask(Object lock) {

            this.lock = lock;
        }

        public void run() {

            synchronized (lock) {


                System.out.println(Thread.currentThread().getName() + " is waiting for being notified.");

                try {
                    lock.wait();

                    System.out.println(Thread.currentThread().getName() + " is notified.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
