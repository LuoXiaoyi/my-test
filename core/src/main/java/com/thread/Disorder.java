package com.thread;

/**
 * @author xiluo
 * @ClassName
 * @description 模拟 CPU 的乱序执行
 * @date 2020/4/30 20:59
 * @Version 1.0
 **/
public class Disorder {

    static int a = 0, b = 0, c = 0, d = 0;

    public static void main(String[] args) throws InterruptedException {
        long count = 0;
        while (true) {
            count++;
            a = 0;
            b = 0;
            c = 0;
            d = 0;

            Thread t1 = new Thread() {
                @Override
                public void run() {
                    b = 1;
                    c = a;
                }
            };

            Thread t2 = new Thread() {
                @Override
                public void run() {
                    a = 1;
                    d = b;
                }
            };

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            if (c == 0 && d == 0) {
                System.err.println("Disorder execution happened at " + count + " times.");
                break;
            }
        }

    }
}
