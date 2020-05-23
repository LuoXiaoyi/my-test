package com.thread;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author xiluo
 * @createTime 2019/2/19 20:01
 **/
public class MyThreadTest {

    public static void main(String[] args) throws Exception{
        testExceptionHandler();
        testThreadPool();
    }


    private static void testExceptionHandler(){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t.getName() + "@" + t.getId());
                System.out.println(Arrays.toString(t.getStackTrace()));
                e.printStackTrace();
            }
        });

        new Thread(()->{
            throw new NullPointerException("120");
        }).start();
    }

    private static void testThreadPool() throws Exception{
        Future<String> future = executorService.submit(new MyTask());
        System.out.println(future.get());
        executorService.shutdown();
    }

    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    static class MyTask implements Callable<String> {

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public String call() throws Exception {
            Thread.sleep(10000);
            return "Hello world.";
        }
    }
}
