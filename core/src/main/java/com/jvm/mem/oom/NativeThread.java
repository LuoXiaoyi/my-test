package com.jvm.mem.oom;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-31 20:52
 **/
public class NativeThread {

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
     * 	at java.lang.Thread.start0(Native Method)
     * 	at java.lang.Thread.start(Thread.java:717)
     * 	at com.jvm.mem.oom.NativeThread.main(NativeThread.java:20)
     * @param args
     */
    public static void main(String[] args) {
        long i = 0;
        while (true) {
            new Thread(() -> {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                }
            }).start();

            System.out.println("create thread: " + i++);
        }

    }

}
