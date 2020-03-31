package com.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/31 14:58
 * @Version 1.0
 **/
public class ReferencesTest {

    // This example shows how having weak references pointing to objects
    // May result in more frequent Full GC pauses
    //
    // There are two modes (controlled by weak.refs)
    //
    //  1. A lot of objects are created
    //  2. A lot of objects are created, and weak references are created
    //     for them. These references are held in a buffer until it's full
    //
    // The allocations made in both cases need to be exactly the same,
    // so in (1) weak references will be also created, but all of them
    // will be pointing to the same object

    // 1. Run with: -XX:+PrintGCTimeStamps -verbose:gc -Xmx24m -XX:NewSize=16m -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails
    //
    //    Observe that there are mostly young GCs
    //
    // 2. Run with: -XX:+PrintGCTimeStamps -Dweak.refs=true -verbose:gc -Xmx24m -XX:NewSize=16m -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails
    //
    //    Observe that there are mostly full GCs
    //
    // 3. Run with: -XX:+PrintGCTimeStamps -Dweak.refs=true -verbose:gc -Xmx64m -XX:NewSize=32m -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails
    //
    //    Observe that there are mostly young GCs

    private static final int OBJECT_SIZE = Integer.getInteger("obj.size", 192);
    private static final int BUFFER_SIZE = Integer.getInteger("buf.size", 64 * 1024);
    private static final boolean WEAK_REFS_FOR_ALL = Boolean.getBoolean("weak.refs");

    private static Object makeObject() {
        return new byte[OBJECT_SIZE];
    }

    public static volatile Object sink;

    public static void main(String[] args) throws InterruptedException {
        System.out.printf("Buffer size: %d; Object size: %d; Weak refs for all: %s%n", BUFFER_SIZE, OBJECT_SIZE, WEAK_REFS_FOR_ALL);

        // We want to create it in both scenarios so the footprint matches
        final Object substitute = makeObject();
        final Object[] refs = new Object[BUFFER_SIZE];
        final ReferenceQueue queue = new ReferenceQueue();

        new Thread(() -> {
            while (true) {
                MyPhantomRef mref = null;
                try {
                    mref = (MyPhantomRef) queue.remove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // System.out.println("id: " + mref.id + ", name: " + mref.name + " be collected.");
            }
        }).start();

        System.gc(); // Clean up young gen

        long id = 0;
        for (int index = 0; ; ) {
            Object object = makeObject();
            // Prevent Escape Analysis from optimizing the allocation away
            sink = object;

            if (!WEAK_REFS_FOR_ALL) {
                object = substitute;
            }

            refs[index++] = new WeakReference<>(object);
            //refs[index++] = new SoftReference<>(object);
            refs[index++] = new MyPhantomRef(object, queue, id++, "test+" + id);

            if (index == BUFFER_SIZE) {
                Arrays.fill(refs, null);
                index = 0;
            }
        }
    }

    static class MyPhantomRef extends PhantomReference {
        private long id;
        private String name;

        public MyPhantomRef(Object referent, ReferenceQueue q, long id, String name) {
            super(referent, q);
            this.id = id;
            this.name = name;
        }
    }
}
