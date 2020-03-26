package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/22 15:14
 **/
public class TimeCostRecoder {

    public static void main(String[] args) throws InterruptedException {
        TimeCostRecoder recoder = new TimeCostRecoder();
        Thread.sleep(1000);
        System.out.println(recoder.getSplitCostTime());
        Thread.sleep(1500);
        System.out.println(recoder.getSplitCostTime());
        System.out.println(recoder.getTotalCostTime());
    }

    private long startTime;
    private long splitTime;

    public TimeCostRecoder() {
        startTime = System.currentTimeMillis();
        splitTime = startTime;
    }

    public long getSplitCostTime() {
        long now = System.currentTimeMillis();
        long delta = now - splitTime;
        splitTime = now;
        return delta;
    }

    public long getTotalCostTime() {
        return System.currentTimeMillis() - startTime;
    }
}
