package com.cpu;

import java.util.Random;

public class HotMethodSimulator {

    public static void main(String[] args) throws InterruptedException {
        HotMethodSimulator simulator = new HotMethodSimulator();
        while (true) {
            Random rm = new Random();
            int r = rm.nextInt();
            simulator.testA(r);
            simulator.testB(r);
            simulator.testC(r);
            simulator.testD(r);

            Thread.sleep(1);
        }
    }

    public void testA(int r) {
        if (r % 2 == 0) {
            testB(r);
        } else {
            testC(r);
        }
    }

    public void testB(int r) {
        if (r % 2 == 0) {
            testC(r);
            testD(r);
        } else {
            testD(r);
        }
    }

    public void testC(int r) {
        if (r % 2 == 0) {
            testD(r);
        } else {
            testB(r);
        }
    }

    public long testD(int r) {
        long sum = 0;
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            sum += i;
        }
        testE(r);
        return sum;
    }

    public long testE(int r) {
        long sum = 0;
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            sum += i;
        }

        return sum;
    }
}
