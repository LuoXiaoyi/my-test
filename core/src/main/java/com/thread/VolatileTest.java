package com.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiluo
 * @date 2020/5/17 00:32
 * @Version 1.0
 **/
public class VolatileTest {
    /**
     * 对象变为 volatile，则成为了 volatile bean，换句话说，bean 中的字段也会变成 volatile 的
     */
    static volatile A a;
    static volatile boolean isRunning;

    public static void main(String[] args) throws Exception {
        a = new A();
        a.isRunning = true;
        isRunning = true;

        for (int i = 0; i < 1; ++i)
            new Thread(new MyTask()).start();

        Thread.sleep(2000);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " change flag to false.");
            a.change();
        }).start();
    }

    static class A {
        boolean isRunning;
        int a = 10;
        int b = 100;
        float c = 100.0f;
        List<String> list = new ArrayList<>();

        void change(){
            isRunning = false;
            a = 100;
            b = 1000;
            c = 1000.0f;
            list.add("abc");
        }

        @Override
        public String toString() {
            return "A{" +
                    "isRunning=" + isRunning +
                    ", a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    ", list=" + list +
                    '}';
        }
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " started.");
            while (a.isRunning) {
                // System.out.println("hello: " + new Date());
            }
            System.out.println(a);
            System.out.println(Thread.currentThread().getName() + " exited.");
        }
    }
}
