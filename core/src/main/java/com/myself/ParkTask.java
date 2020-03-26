package com.myself;

import java.util.concurrent.locks.LockSupport;

/**
 * @author xiaoyiluo
 * @createTime 28/05/2018 2:31 PM
 **/
public class ParkTask implements Runnable {

    private Object lock;
    private boolean isTimedPark = false;

    public ParkTask(boolean isTimedPark, Object lock) {
        this.isTimedPark = isTimedPark;
        this.lock = lock;
    }

    public void run() {

        synchronized (lock) {

            System.out.println(Thread.currentThread().getName() + " entering ParkTask...");

            if (isTimedPark)
                LockSupport.parkNanos(Long.MAX_VALUE);
            else {
                LockSupport.park();
                System.out.println(" parking .... ");
            }
        }

    }
}
