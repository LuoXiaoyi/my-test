package com.thread;

import sun.misc.Unsafe;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/5/22 20:38
 * @Version 1.0
 **/
public class ThreadPoolTest {

    public static void main(String[] args) {
        //System.out.println(test());
        /*ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 4, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(16));

        while (true) {
            executor.execute(() -> {
                int a = 0;
                ++a;
            });
        }*/

        Unsafe unsafe = Unsafe.getUnsafe();
        unsafe.allocateMemory(100);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(ThreadPoolTest::test,0, 5, TimeUnit.SECONDS);

        int abc = 100;
        System.out.println(abc);
    }

    static int test() {
        System.out.println(System.currentTimeMillis() + " -> thread: " + Thread.currentThread());
        int flag = 0;
        try {
            Thread.sleep(100_000);
            return test2();
        } catch (Exception e) {
            flag = 1;
            return flag;
        } finally {
            flag = 2;
        }
    }

    static int test2() {
        throw new UnsupportedOperationException("test2");
    }

}
