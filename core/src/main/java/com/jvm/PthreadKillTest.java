package com.jvm;

import java.net.ServerSocket;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-15 10:01
 **/
public class PthreadKillTest {

    /**
     * -XX:-Inline
     * -XX:+PrintSafepointStatistics
     * -XX:PrintSafepointStatisticsCount=1
     * -XX:+PrintGCApplicationStoppedTime
     * -XX:+UseCountedLoopSafepoints
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        PthreadKillTest test = new PthreadKillTest();

        // Thread.sleep(10000);
        new Thread(() -> {
            while (true) {
                test.test0();
                try {
                    //ServerSocket ss = new ServerSocket(10000);
                    //ss.accept();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    void test0() {
        test1();
    }

    void test1() {
        test2();
    }


    void test2() {
        test3();
    }

    void test3() {
        test4();
    }

    void test4() {
        test5();
    }

    void test5() {
        System.out.println("pthread_kill test");
    }

}
