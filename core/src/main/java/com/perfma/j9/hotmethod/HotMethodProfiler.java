package com.perfma.j9.hotmethod;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * openj9
 *
 * @author xiluo
 * @createTime 2019-11-27 17:03
 **/
final public class HotMethodProfiler {
    public static int getProfileLwpId(){
        int i = LWP_ID_BUF.getInt();
        LWP_ID_BUF.rewind();
        return i;
    }

    /**
     * @return 用于获取当前正在 CPU 上执行的线程的 native id，即 LWP id
     */
    public static native int profileOsThreadId();

    /**
     * @return 用于获取当前线程的 native id，即 LWP id
     */
    public static native int currentOsThreadId();

    /**
     * 1. 重置 profiler 线程id，每次进行完爬栈之后，务必执行该方法，防止同一个线程的栈在一次采样中被采集多次
     * 2. 爬取当前 profile 线程的堆栈
     */
    public static native void triggerStackTraceDumpIfNecessary();

    /**
     * only read by native code
     */
    private static final long PROFILE_LWP_ID_ADDR;
    private static final ByteBuffer LWP_ID_BUF;

    static {
        LWP_ID_BUF = ByteBuffer.allocateDirect(4);
        String fName = "address";
        Class<?> crtClazz = LWP_ID_BUF.getClass();
        Field field = null;
        while (field == null && crtClazz != Object.class) {
            try {
                field = crtClazz.getDeclaredField(fName);
            } catch (NoSuchFieldException e) {
                crtClazz = crtClazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new RuntimeException(fName + " not found.");
        }

        boolean acc = field.isAccessible();
        if (!acc) {
            field.setAccessible(true);
        }

        try {
            PROFILE_LWP_ID_ADDR = field.getLong(LWP_ID_BUF);
            System.out.println("PROFILE_LWP_ID_ADDR: " + PROFILE_LWP_ID_ADDR);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can not get address value.");
        }

        if (!acc) {
            field.setAccessible(false);
        }
    }
}
