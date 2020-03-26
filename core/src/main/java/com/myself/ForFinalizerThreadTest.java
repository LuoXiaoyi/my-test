package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 30/05/2018 17:11
 **/
public class ForFinalizerThreadTest {

    private byte[] bigMem = null;
    private Object lock = null;
    public ForFinalizerThreadTest(Object lock){
        int s =1024*20;
        bigMem = new byte[s]; // 20MB
        this.lock = lock;
        // System.out.println("lock : " + lock);
    }


    protected void finalize() throws Throwable {

        System.out.println(Thread.currentThread().getName() +  " ################## entering finalize...  ###################### ");
        long begin = System.currentTimeMillis();

        /*while(System.currentTimeMillis() - begin < 1000*1000){

            Thread.sleep(10);
        }*/
        synchronized (lock){
            System.out.println(" got the lock ....");
        }

        System.out.println("left finalize... ");
    }
}
