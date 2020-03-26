package com.myself;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-06-15 21:13
 **/
public class XlionByteTest {


    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(
                () -> {
                    try {
                        ServerSocket ss = new ServerSocket();
                        System.out.println("server started.");
                        ss.bind(new InetSocketAddress(8089));
                        ss.accept();
                    } catch (IOException ioe) {
                    }

                    System.out.println("thread will quit...");
                });
        t.start();

        Thread.sleep(10000);
        t.interrupt();
        System.out.println("after interrupt");
    }
}
