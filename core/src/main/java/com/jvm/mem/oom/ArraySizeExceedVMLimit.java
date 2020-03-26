package com.jvm.mem.oom;

/**
 * test
 * <p>
 * java.lang.OutOfMemoryError: Requested array size exceeds VM limit
 *
 * @author xiluo
 * @createTime 2019-10-31 21:35
 **/
public class ArraySizeExceedVMLimit {

    /**
     * It happens because the 2^31-1 int primitives you are trying to make room for
     * require 8G of memory which is less than the defaults used by the JVM.
     *
     * @param args
     */
    public static void main(String[] args) {

        for (int i = 3; i >= 0; i--) {
            try {
                int[] arr = new int[Integer.MAX_VALUE - i];
                System.out.format("Successfully initialized an array with %,d elements.\n", Integer.MAX_VALUE - i);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
