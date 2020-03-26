package com.myself;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/22 14:16
 **/
public class ThreadPoolRejectionTest {
    static AtomicInteger ai = new AtomicInteger(1);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                System.out.println("i: " + (i + 1));
                DEL_FASTDFS_EXECUTOR.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("execute... " + ai.getAndIncrement());
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
            }
        }, "test-thread").start();

        System.out.println(" end ..... ");
    }

    private static ThreadPoolExecutor DEL_FASTDFS_EXECUTOR =
            new ThreadPoolExecutor(
                    2,
                    4,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(10),
                    new ThreadFactory() {
                        private AtomicInteger ai = new AtomicInteger(1);

                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, "test-" + ai.getAndIncrement());
                        }
                    },
                    (Runnable r, ThreadPoolExecutor executor) -> {
                        try {
                            System.out.println(Thread.currentThread().getName());
                            System.out.println("Oops!! the thread pool is full, will re-execute the task." + executor);
                            Thread.sleep(10);
                            executor.execute(r);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
}
