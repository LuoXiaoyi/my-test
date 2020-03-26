package com.bytecode.asm;

import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-11-06 16:29
 **/
public class TT {
    public static void main(String[] args) throws Exception {
        while (true) {
            TT t = new TT();

            Method m = t.getClass().getDeclaredMethod("test");

            // 获取方法返回值的类型
            ParameterizedType genericReturnType = (ParameterizedType) m.getGenericReturnType();
            java.lang.reflect.Type[] actualTypeArguments = genericReturnType.getActualTypeArguments();

            Class<?> c = m.getReturnType();

            Field f = m.getClass().getDeclaredField("signature");
            f.setAccessible(true);

            Object o = f.get(m);

            //Type type = Type.getReturnType(m);
            // Type.getReturnType("()Ljava/util/List<Ljava/lang/String;>;");
            Type type = Type.getReturnType((String) o);
            System.out.println(type.getDescriptor());
            System.out.println(type.getClassName());
            System.out.println(type);
            Thread.sleep(1000);
        }
    }

    private List<String> test() {
        return null;
    }
}
