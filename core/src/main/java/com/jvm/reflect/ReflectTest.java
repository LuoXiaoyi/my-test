package com.jvm.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xiluo
 * @createTime 2019/4/11 20:09
 **/
public class ReflectTest {

    public static void trace(int seq) {
        new Exception("#" + seq).printStackTrace();
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method m = ReflectTest.class.getMethod("trace", int.class);
        Method m2 = ReflectTest.class.getMethod("trace", int.class);

        System.out.println(m == m2);
        System.out.println(m.equals(m2));

        for (int i = 0; i < 20; ++i)
            m.invoke(null, i+1);
    }
}
