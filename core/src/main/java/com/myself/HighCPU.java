package com.myself;

/**
 * @author xiaoyiluo
 * @createTime 24/05/2018 2:26 PM
 **/
public class HighCPU{

    public static void main(String[] args) {

        Thread t1 = new Thread(new Task(true));
        t1.setName("busy thread");
        Thread t2 = new Thread(new Task(false));
        t2.setName("idle thread");
        t1.start();
        t2.start();
    }

    private static class Task implements Runnable{

        private boolean willAlwaysRun = false;

        public Task(boolean willAlwaysRun){
            this.willAlwaysRun = willAlwaysRun;
        }

        public void run() {

            if(willAlwaysRun){
                this.alwaysRun();
            }else {
                int i = 0;
                while (i < 100){
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    ++i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void alwaysRun(){
            while (true){
                System.out.println(Thread.currentThread().getName() + " is running...");
            }
        }
    }
}
