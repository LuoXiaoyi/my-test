package com.perfma.j9.hotmethod;

/**
 * openj9
 * <p>
 * -XX:-Inline
 * -agentpath:/Users/xiaoyiluo/Documents/PerfMa/code/profiler/perfma-profiler-cpu-j9/build/libperfma_cpu_j9-1.0-macos-x64.dylib
 *
 * @author xiluo
 * @createTime 2019-11-13 19:20
 **/
public class RuntimeTest {

    public static void main(String[] args) throws Exception {
        new Thread(
                new Runnable() {
                    public void run() {
                        while (true) {
                            test1(1);
                        }
                    }
                }).start();
    }

    static void test1(int a) {
        test2(a);
    }

    static void test2(int a) {
        test3(a);
    }

    static void test3(int a) {
        test4(a);
    }

    static void test4(int a) {
        test5(a);
    }

    static void test5(int a) {
        test6(a);
    }

    static void test6(int a) {
        test7(a);
    }

    static void test7(int a) {
        test8(a);
    }

    static void test8(int a) {
        test9(a);
    }

    static void test9(int a) {
        test10(a);
    }

    static void test10(int a) {
        //System.out.println(HotMethodProfiler.getProfileLwpId());
        // nothing.
        //HotMethodProfiler.triggerStackTraceDumpIfNecessary();
    }
}
