package com.jvm.mem.oom;

import javassist.ClassPool;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-31 20:10
 **/
public class Metaspace {
    /**
     * -XX:MaxMetaspaceSize=64m
     *
     * Exception in thread "main" javassist.CannotCompileException: by java.lang.ClassFormatError: Metaspace
     * 	at javassist.util.proxy.DefineClassHelper.toClass(DefineClassHelper.java:271)
     * 	at javassist.ClassPool.toClass(ClassPool.java:1240)
     * 	at javassist.ClassPool.toClass(ClassPool.java:1098)
     * 	at javassist.ClassPool.toClass(ClassPool.java:1056)
     * 	at javassist.CtClass.toClass(CtClass.java:1298)
     * 	at com.jvm.mem.oom.Metaspace.generate(Metaspace.java:25)
     * 	at com.jvm.mem.oom.Metaspace.main(Metaspace.java:19)
     * Caused by: java.lang.ClassFormatError: Metaspace
     * 	at javassist.util.proxy.DefineClassHelper$Java7.defineClass(DefineClassHelper.java:182)
     * 	at javassist.util.proxy.DefineClassHelper.toClass(DefineClassHelper.java:260)
     *
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
