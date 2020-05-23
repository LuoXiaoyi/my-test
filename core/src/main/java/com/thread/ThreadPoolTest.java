package com.thread;

import java.util.concurrent.LinkedBlockingDeque;
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
        System.out.println(test());
        /*ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 4, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(16));

        while (true) {
            executor.execute(() -> {
                int a = 0;
                ++a;
            });
        }*/

    }

    static int test() {
        int flag = 0;
        try {
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
