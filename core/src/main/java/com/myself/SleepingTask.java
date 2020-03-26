package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 28/05/2018 2:31 PM
 **/
public class SleepingTask implements Runnable {

    private Object lock;

    public SleepingTask(Object lock){
        this.lock = lock;
    }

    public void run(){

        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + " entering SleepingTask...");

            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
