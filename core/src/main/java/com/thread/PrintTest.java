package com.thread;

/**
 * @author xiluo
 * @ClassName
 * @description 线程 A 、B 循环打印 ABBBBABBBBABBBB....
 * @date 2020/5/13 17:45
 * @Version 1.0
 **/
public class PrintTest {

    public static void main(String[] args) {
        Object sync = new Object();
        new Thread(new TaskA(sync)).start();
        new Thread(new TaskB(sync)).start();
    }

    static volatile boolean start = true;
    static volatile int seq = 0;

    static class TaskA implements Runnable {
        final Object sync;

        TaskA(Object sync) {
            this.sync = sync;
        }

        @Override
        public void run() {
            while (start) {
                while (seq != 0) {
                }

                synchronized (sync) {
                    System.out.print("A");
                    seq = 1;
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class TaskB implements Runnable {
        final Object sync;

        TaskB(Object sync) {
            this.sync = sync;
        }

        @Override
        public void run() {
            while (start) {
                while (seq != 1) {
                }
                synchronized (sync) {
                    for (int i = 0; i < 4; ++i) {
                        System.out.print("B");
                    }
                    sync.notifyAll();
                    seq = 0;
                }
            }
        }
    }

}
