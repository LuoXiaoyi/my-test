package com;

import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-08-23 14:34
 **/
public class ByteOrderTest {

    public static void main(String[] args) throws IOException {
        double total = 260;
        System.out.println(total * (0.04 + 0.02 + 0.02) + total * (1 - 0.45) * 0.04 + total * 0.45);

        /*System.out.println("user.dir: " + System.getProperty("user.dir"));
        int a = 0xCAFEBABE;
        long b = 0x0123456789abcdefL;
        System.out.println("a: " + a + ", b: " + b);
        File f = new File(System.getProperty("user.dir") + "/byteorder_java.txt");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
        dos.writeInt(a);
        dos.writeLong(b);
        dos.close();*/

        ByteBuffer.allocateDirect(1024);
        MappedByteBuffer.allocateDirect(1024);

    }

}
