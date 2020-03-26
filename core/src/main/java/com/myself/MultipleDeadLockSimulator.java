package com.myself;

public class MultipleDeadLockSimulator {


    public static void main(String[] args) {
        Thread t1 = new Thread(new DeadLockTask(0));
        Thread t2 = new Thread(new DeadLockTask(1));
        Thread t3 = new Thread(new DeadLockTask(2));
        Thread t4 = new Thread(new DeadLockTask(3));
        Thread t5 = new Thread(new DeadLockTask(3));
        Thread t6 = new Thread(new DeadLockTask(0));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        // for cpu usage test
        Thread t7 = new Thread(new Runnable() {
            long i = 0;
            @Override
            public void run() {
                while(true){
                    ++i;
                }
            }
        });
        t7.start();

        // for mem usage test
        Thread t8 = new Thread(new Runnable() {
            long i = 0;
            @Override
            public void run() {
                while(true){
                  char[] a = new char[1024];
                }
            }
        });
        t8.start();
    }

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();
    private static Object lock3 = new Object();
    private static Object lock4 = new Object();

    private static class DeadLockTask implements Runnable{

        private int flag = 0;

        public DeadLockTask(int f){
            this.flag = f;
        }

        public void run() {
            while(true){
                switch (flag){
                    case 0:
                        synchronized (lock1){

                            System.out.println(Thread.currentThread().getName() + " hold lock1");

                            synchronized (lock2){
                                System.out.println(Thread.currentThread().getName() + " hold lock2");
                            }
                        }
                        break;
                    case 1:
                        synchronized (lock2){

                            System.out.println(Thread.currentThread().getName() + " hold lock2");

                            synchronized (lock3){
                                System.out.println(Thread.currentThread().getName() + " hold lock3");
                            }
                        }
                        break;
                    case 2:
                        synchronized (lock3){

                            System.out.println(Thread.currentThread().getName() + " hold lock3");

                            synchronized (lock4){
                                System.out.println(Thread.currentThread().getName() + " hold lock4");
                            }
                        }
                        break;
                    case 3:
                        synchronized (lock4){

                            System.out.println(Thread.currentThread().getName() + " hold lock4");

                            synchronized (lock1){
                                System.out.println(Thread.currentThread().getName() + " hold lock1");
                            }
                        }
                        break;
                }
            }
        }
    }
}
