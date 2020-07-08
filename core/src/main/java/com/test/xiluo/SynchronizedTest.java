package com.test.xiluo;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author xiluo
 * @ClassName
 * @description 输出的第一行内容和锁状态内容对应
 * unused:1 | age:4 | biased_lock:1 | lock:2
 * 0           0000             0                01     代表A对象正处于无锁状态
 * <p>
 * 第三行中表示的是被指针压缩为32位的klass pointer
 * 第四行则是我们创建的A对象属性信息 1字节的boolean值
 * 第五行则代表了对象的对齐字段 为了凑齐64位的对象，对齐字段占用了3个字节，24bit
 *
 * 链接：https://www.cnblogs.com/LemonFive/p/11246086.html
 *
 * @date 2020/7/8 16:47
 * @Version 1.0
 **/
public class SynchronizedTest {

    private int age;

    public static void main(String[] args) {
        SynchronizedTest a = new SynchronizedTest();
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
