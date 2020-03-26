package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 04/06/2018 22:18
 **/
public class ThreadTest {


    public static void main(String[] args) {

        test2();
    }

    public static void test2() {

        Thread t = new Thread(new Runnable() {
            Object lock = new Object();

            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (lock) {
                        System.out.println(" running... , time: " + System.currentTimeMillis());
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.out.println("  ---->   " + Thread.currentThread().isInterrupted());
                            System.out.println(" InterruptedException.... ");
                            /**
                             * 抛出时要注意？？？：当你捕获到InterruptedException异常后，当前线程的中断状态已经被修改为false(表示线程未被中断)；
                             * 此时你若能够处理中断，则不用理会该值；但如果你继续向上抛InterruptedException异常，你需要再次调用interrupt方法，
                             * 将当前线程的中断状态设为true。
                              90o0-o0-o0p-ol  *
                             * 注意：绝对不能“吞掉中断”！即捕获了InterruptedException而不作任何处理。这样违背了中断机制的规则，别人想让你线程中断，
                             * 然而你自己不处理，也不将中断请求告诉调用者，调用者一直以为没有中断请求。
                             *
                             * 再次调用 interrupt 设置中断位为 true
                             */
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                System.out.println(" out while.... ");
            }
        });

        t.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
//        if (t.isInterrupted()) { // 加上这一段就能正常退出了
//            System.out.println(t.getName() + " has been interrupted");
//        }
//        t.interrupt();
//        System.out.println(" +++++++>  " + t.isInterrupted());
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void test1() {
        Thread t = new Thread(new Runnable() {
            Object lock = new Object();

            public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                    /*synchronized (lock) {
                        try {
                            System.out.println("hahah.... ");
                            // Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("InterruptedException .....");
                            System.out.println("----> " + Thread.currentThread().isInterrupted());
                        }
                    }*/
                }

                System.out.println(" out while ");

            }
        });

        t.start();
        System.out.println("  ***********> " + t.isInterrupted());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("begin to interrupt thread: " + t.getName());
        t.interrupt();
//        if (t.isInterrupted()) {
//            System.out.println(t.getName() + " has been interrupted");
//        }
//        t.interrupt();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
