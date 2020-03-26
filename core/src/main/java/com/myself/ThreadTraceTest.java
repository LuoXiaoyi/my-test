package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 2018/8/5 13:31
 **/
public class ThreadTraceTest{

    private static class Task implements Runnable{

        public void run(){
            System.out.println("run : " + Thread.currentThread().getName());
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{

        Thread t1 = new Thread(new Task());
        t1.start();

        Thread t2 = new Thread(new Task());
        t2.start();

        int activeCount = t1.getThreadGroup().activeCount();
        Thread[] list = new Thread[activeCount];
        System.out.println("activeCount: " + activeCount);

        int count = t1.getThreadGroup().enumerate(list);

        System.out.println("count: " + count);

        for(Thread t: list){
            System.out.println(t.getId() + " : " + t.getName());
        }


        t1.join();
        t2.join();

        Thread.sleep(10000000);
    }

}
