package com.bytecode.newi;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * test
 * -agentpath:/Users/xiaoyiluo/Documents/my-space/code/object-size/libnew.dylib
 * @author xiluo
 * @createTime 2019-09-24 09:59
 **/
public class NewInstruction {

    public static void main(String[] args) throws Throwable {
        //System.out.println(ObjectHelper.registerTest());
        //testOper();
        testIo();
    }

    static void testOper() throws Exception{
        while (true) {
            newOper();
            Thread.sleep(1000);
        }
    }

    static void testIo() throws Throwable {
        final String testFile = "/Users/xiaoyiluo/Desktop/YinxiangBiji_RELEASE_9.1.3_458534.dmg";
        FileInputStream fis = new FileInputStream(testFile);
        int size = fis.available();
        while (size-- > 0) {
            try {
                fis.read();
            } catch (IOException io) {
                io.printStackTrace();
                fis.close();
                System.out.println("re-open file: " + testFile);
                fis = new FileInputStream(testFile);
                size = fis.available();
            } finally {
                Thread.sleep(500);
            }
        }

        fis.close();
    }

    static void exception() throws Throwable {
        // 异常则通过注入 Throwable 来获取堆栈吧，但是对未 catch 的和 catch 的好像无法区分
        Class<?> tw = Class.forName("java.lang.Throwable");
        System.out.println(tw.getClass());
    }

    static void nativeAllocate() {
        ByteBuffer.allocateDirect(1000);
    }

    /**
     * 0: new           #2                  // class java/lang/Object
     * 3: dup
     * 4: invokespecial #1                  // Method java/lang/Object."<init>":()V
     * 7: astore_1
     * 8: bipush        10
     * 10: newarray       int
     * 12: astore_2
     * 13: bipush        10
     * 15: iconst_5
     * 16: multianewarray #3,  2             // class "[[I"
     * 20: astore_3
     * 21: iconst_2
     * 22: anewarray     #2                  // class java/lang/Object
     * 25: astore        4
     * 27: iconst_1
     * 28: iconst_2
     * 29: multianewarray #4,  2             // class "[[Ljava/lang/Object;"
     * 33: astore        5
     */
    static void newOper() {
        /**
         *          0: new           #2                  // class java/lang/Object
         *          3: dup
         *          4: invokespecial #1                  // Method java/lang/Object."<init>":()V
         *          7: astore_1
         */
        Object o = new Object();
        long size = ObjectHelper.getObjectSizeOut(o);
        System.out.println("Object: " + o + " size: " + size);
        /**
         *          8: bipush        10
         *         10: newarray       int
         *         12: astore_2
         */
        int[] i = new int[10];
        size = ObjectHelper.getObjectSizeOut(i);
        System.out.println("int[]: " + i + " size: " + size);

        /**
         *         13: bipush        10
         *         15: iconst_5
         *         16: multianewarray #3,  2             // class "[[I"
         *         20: astore_3
         */
        int[][] i2 = new int[10][5];
        size = ObjectHelper.getObjectSizeOut(i2);
        System.out.println("int[][]:" + i2 + " size: " + size);

        /**
         *         21: iconst_2
         *         22: anewarray     #2                  // class java/lang/Object
         *         25: astore        4
         *
         */
        Object[] oa = new Object[2];
        size = ObjectHelper.getObjectSizeOut(oa);
        System.out.println("Object[] " + oa + " size: " + size);

        /**
         *          27: iconst_1
         *          28: iconst_2
         *          29: multianewarray #4,  2             // class "[[Ljava/lang/Object;"
         *          33: astore        5
         */
        Object[][] oa2 = new Object[1][2];
        size = ObjectHelper.getObjectSizeOut(oa2);
        System.out.println("Object[][] " + oa2 + " size: " + size);
    }

    static class Test {
        int age;
        Map<String, String> name2age;

        public Test() {
            name2age = new HashMap<>();
            name2age.put("xiluo", "cpu");
            System.out.println("map size: " + ObjectHelper.getObjectSizeOut(name2age));
        }
    }
}
