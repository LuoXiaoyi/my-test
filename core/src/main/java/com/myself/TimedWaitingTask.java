package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 28/05/2018 2:31 PM
 **/
public class TimedWaitingTask implements Runnable {

    private Object lock = new Object();

    private boolean isTimed;

    public TimedWaitingTask(boolean isTimed) {
        this.isTimed = isTimed;
    }

    public void run() {

        synchronized (lock) {


            System.out.println("entering TimedWaitingTask...");

            try {
                if (isTimed)
                    lock.wait(Integer.MAX_VALUE);
                else
                    lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
