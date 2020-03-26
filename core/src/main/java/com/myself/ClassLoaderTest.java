package com.myself;

import java.lang.reflect.Method;

/**
 * @author xiluo
 * @createTime 2019/2/20 21:08
 **/
public class ClassLoaderTest {
    static int idx = 0;

    public static void main(String[] args) throws Exception {
        while (idx < 25) {
            System.out.println("abc");
            Thread.sleep(1000);

            idx++;

            if (idx > 20) {
                // StringBuffer.class loaded
                StringBuffer sb = new StringBuffer();

                // MyClassLoader
                MyClassLoader classLoader = new MyClassLoader("/Users/xiaoyiluo/Desktop");
                Class<?> testClazz = classLoader.loadClass("Test");

                Object obj = testClazz.newInstance();
                Method foo = testClazz.getDeclaredMethod("foo");
                foo.invoke(obj);

                System.out.println(obj.getClass().getClassLoader());
                classLoader = null;

                break;
            }
        }

        System.gc();
        System.out.println("gc...");
        Thread.sleep(100000);
    }
}
