package com.jvm.mem.oom;

import javassist.ClassPool;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-31 19:11
 **/
public class PermgenSpace {

    /**
     * JDK 8 has removed Perm area
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100_000_000; i++) {
            generate("com.perfma.jvm.mem.oom.Generated" + i);
        }
    }

    public static Class generate(String name) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        return pool.makeClass(name).toClass();
    }
}
