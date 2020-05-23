package com.jvm;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/5/18 00:35
 * @Version 1.0
 **/
public class ClassLoaderTest {

    public static void main(String[] args) {
        System.out.println(A.ct);
        int i = 8;

        i = i++;
//        i = ++i;
        System.out.println(i);
    }

    static class A {
        static A a = new A();
        static int ct = 0;

        A() {
            ct = 1;
        }
    }

}
