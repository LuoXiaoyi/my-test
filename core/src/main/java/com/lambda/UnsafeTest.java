package com.lambda;

import sun.misc.Unsafe;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author xiluo
 * @createTime 2019/3/3 22:33
 **/
public class UnsafeTest {

    public static void main(String args[]) throws Throwable {
        System.out.println(UnsafeTest.class.getCanonicalName());


        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        String filePath = "/Users/xiaoyiluo/AA.class";
        byte[] buffer = getFileContent(filePath);
        Class<?> c1 = unsafe.defineAnonymousClass(UnsafeTest.class, buffer, null);
        Class<?> c2 = unsafe.defineAnonymousClass(UnsafeTest.class, buffer, null);

        System.out.println("c1: " + c1.getCanonicalName() + ", hs: " + c1.hashCode());
        System.out.println("c2: " + c2.getCanonicalName() + ", hs: " + c2.hashCode());

        System.out.println(c1 == c2);
    }

    static byte[] getFileContent(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        byte[] ret = new byte[bis.available()];
        bis.read(ret);
        return ret;
    }
}
