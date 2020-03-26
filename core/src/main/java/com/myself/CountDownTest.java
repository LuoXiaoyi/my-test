package com.myself;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiluo
 * @createTime 2019/1/9 10:48
 **/
public class CountDownTest {
    static final int taskNbr = 10;
    //使用场景：在主线程中开启多个任务，并且主线程需要等待其他子任务全部执行完毕之后才能汇总结果继续执行
    private static volatile CountDownLatch countDownLatch = new CountDownLatch(taskNbr);
    static long s1 = 0;
    static long sa[] = new long[taskNbr];

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(taskNbr, taskNbr, 10,
                TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        threadPoolExecutor.prestartAllCoreThreads(); //预先将核心线程都启动起来

        s1 = System.currentTimeMillis();
        for (int i = 0; i < taskNbr; i++) {
            threadPoolExecutor.execute(new TTask(i));
        }

        countDownLatch.await();
        long cost = System.currentTimeMillis() - s1;

        System.out.println("cost:" + cost);
        System.out.println("s1 : " + s1);
        for (int i = 0; i < sa.length; ++i) {
            System.out.println(sa[i]);
        }
        threadPoolExecutor.shutdownNow();
    }

    static class TTask implements Runnable {
        int i;

        TTask(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                sa[i] = System.currentTimeMillis();
                Thread.sleep(1000);
            } catch (Exception e) {

            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
