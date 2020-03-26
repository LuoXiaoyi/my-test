package com.bytecode.newi;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-24 11:12
 **/
public class ObjectHelper {
    static ObjectHelper DEFAULT = new ObjectHelper();

    /**
     * 对本地方法和本地方法的实现函数进行注册
     */
    private static native void registerNative();

    static {
        registerNative();
    }

    public <T> T sayHi(int a) {
        return (T) null;
    }

    public static void empty() {
    }

    /**
     * 通过调用 jni 的 RegisterNatives 来调用到此本地方法，这种方法比较适合使用硬编码
     *
     * @return 对象的大小
     */
    public static native int registerTest();

    private native long getObjectSize(Object obj);

    public static long getObjectSizeOut(Object obj) {
        return DEFAULT.getObjectSize(obj);
    }

    /**
     * 原本假设该方法为本地方法，现在被增强之后，该方法为非本地方法，然后调用已经被增强的本地方法
     * @param obj 传入的对象
     * @return 对象的大小
     */
    //public static long getObjectSize(Object obj) {
    //  System.out.println("invoke getObjectSize");
    //  return $$perfma_wrapper$$_getObjectSize(obj);
    //}

    /**
     * 通过添加前缀的方式进行本地方法的调用，这种方法适合采用字节码增强技术对本地方法进行增强     *
     * 获取目标对象的大小
     *
     * @param obj 目标对象
     * @return 返回目标对象大小
     */
    ///private static native long $$perfma_wrapper$$_getObjectSize(Object obj);
}
