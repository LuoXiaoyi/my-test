package com.bytecode.share;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-23 14:13
 **/
public class Listener {
    private static ThreadLocal<Long> timer = new ThreadLocal<>();

    public static void onMethodEnter() {
        timer.set(System.currentTimeMillis());
    }

    public static void onMethodExit() {
        long now = System.currentTimeMillis();
        System.out.println("cost: " + (now - timer.get()));
    }
}
