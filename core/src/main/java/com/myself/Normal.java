package com.myself;

public class Normal {

    public static void main(String[] args) throws InterruptedException {

        int i = 0;
        while(i < 100){
            Thread.sleep(1000);
            ++i;
        }
    }
}
