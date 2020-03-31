package com.myself;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-24 01:03
 **/
public class Test2 {

    public static void main(String[] args) throws Exception {
        byte[] by = new byte[1047552];

        int a = 857382;
        System.out.println(145419558%1047552);

        File file = new File("/Users/xiaoyiluo/wKgAQlzmME2EF-7pAAAAAAAAAAA.rt-cpu.xowl");
        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
        System.out.println(file.length());
//        System.out.println(file.length());
//        int count = fileInputStream.available() / 1047552 + 1;
//        fileInputStream.close();
//        long fileSize = 0L;
        int read;
//        for (int i = 0; i < count; i++) {
//            FileInputStream inputStream = new FileInputStream(file);
//            inputStream.skip(i * 1047552);
//            read = inputStream.read(by);
//            fileSize += read;
//            System.out.println("read byte size is " + read + "     " + i);
//            if (read != -1 && read < 1047552) {
//                byte[] b = new byte[read];
//                System.arraycopy(by, 0, b, 0, read);
//                System.out.println(b.length);
//                System.out.println("File size is " + fileSize);
//                System.out.println(inputStream.available());
//            }
//        }
        File t = new File("/Users/xiaoyiluo/test.bin2");
        BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(t));
        String fileKey = "group1/M00/09/25/wKgAQlzmxvGEae6uAAAAAAAAAAA64455.t";
        int i = 0;
        while ((read = fileInputStream.read(by)) != -1) {
            if(read != 1047552)
                System.out.println("read    "+read);
            if (read < 1047552) {
             //   i++;
                /*byte[] b = new byte[read];
                System.out.println(b.length);
                System.arraycopy(by, 0, b, 0, read);*/
                //FastDFSUtil.append(fileKey, b);
                fileOutputStream.write(by,0,read);
                //System.out.println("count is " + i);
            }
            //i++;
//            System.out.println("read byte size is " + read);
            else fileOutputStream.write(by);
            //FastDFSUtil.append(fileKey, by);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        System.out.println(t.length());
    }

}
