package com.test.xiluo;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/6/3 19:53
 * @Version 1.0
 **/
public class ZipTest {

    public static void main(String[] args) throws IOException {
        testZip2();
    }

    static void testZip2() throws IOException {
        File dir = new File("./test/");
        //ZipUtil.zipFiles(new File("/Users/xiaoyiluo/t/abc.zip"), "hello", dir.listFiles());
        ZipUtil.decompress("/Users/xiaoyiluo/t/abc.zip","111");
    }

    static void testZip() throws IOException {
        /* ZipUtil.zip(new File("/Users/xiaoyiluo/t/h2test.mv.db.zip"), false,
                new File("h2test.mv.db"));
        */

        File outputFile = new File("/Users/xiaoyiluo/t/h2test.mv.db.zip");
        File inputFile = new File("/Users/xiaoyiluo/Documents/my-space/code/test/h2test.mv.db");

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFile));
        zos.putNextEntry(new ZipEntry("123/" + inputFile.getName()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));

        byte[] buffer = new byte[1024 * 1024];
        int cnt = 0;
        while ((cnt = bis.read(buffer)) > 0) {
            zos.write(buffer, 0, cnt);
        }
        zos.closeEntry();
        zos.close();
        bis.close();
    }
}
