//package com.myself;
//
//import com.sun.tools.attach.VirtualMachine;
//import com.sun.tools.attach.VirtualMachineDescriptor;
//import sun.misc.Unsafe;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.lang.ref.Reference;
//import java.lang.ref.ReferenceQueue;
//import java.lang.ref.WeakReference;
//import java.lang.reflect.Field;
//import java.util.List;
//
///**
// * @author xiaoyiluo
// * @createTime 2018/11/8 11:59
// **/
//public class Test {
//
//    public static void main(String[] args) throws Throwable {
//
//        ReferenceQueue<String> queue = new ReferenceQueue<>();
//
//        String obj = new String("hello world");
//        WeakString wref = new WeakString(obj, queue);
//
//        /*new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        for (Object x; (x = queue.poll()) != null; ) {
//                            synchronized (queue) {
//                                System.out.println("2: " + x);
//                            }
//                        }
//                        Thread.sleep(1000);
//                    }catch (Throwable t){}
//                }
//            }
//        }).start();*/
//
//        System.out.println(wref.get());
//
//        obj = null;
//
//        System.gc();
//
//        System.out.println("after gc: " + wref.get());
//
//        Thread.sleep(10000);
//
//        for (Object x; (x = queue.poll()) != null; ) {
//            synchronized (queue) {
//                System.out.println("2: " + x);
//                System.out.println("2: " + ((Reference) x).get());
//            }
//        }
//
//
//    }
//
//    static class WeakString extends WeakReference<String>{
//
//        public WeakString(String referent, ReferenceQueue<String> q) {
//            super(referent, q);
//        }
//    }
//
//    static void test2() throws Throwable {
//        System.out.println(String.class.getName());
//        System.out.println(String.class);
//        System.out.println(String.class.getCanonicalName());
//        System.out.println(String.class.getTypeName());
//        System.out.println(String[].class.getTypeName());
//        System.out.println(String.class.getTypeName());
//
//        System.out.println(A.class.getName());
//
//        System.out.println(int.class);
//
////        String line = "[[Ljava/lang/Object;";
////        String line = "LTest;";
//        String line = "[J";
//        int idx = 0;
//        while (line.charAt(idx++) == '[') ;
//
//        line = line.replace("/", ".");
//        if (line.length() - 1 > idx) {
//            line = line.substring(idx, line.length() - 1);
//            System.out.println("line: " + line);
//        }
//
//        Class<?>[] cs = new Class[100000];
//        for (int i = 0; i < cs.length; ++i) {
//            cs[i] = String.class;
//        }
//        long l = System.currentTimeMillis();
//        File f = new File("./test-classes");
//        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//
//        for (Class<?> c : cs) {
//            bw.write(c.getName());
//            bw.write("\n");
//        }
//
//        bw.close();
//        System.out.println("cost: " + (System.currentTimeMillis() - l));
//
//        /*List<VirtualMachineDescriptor> vms = VirtualMachine.list();
//        System.out.println(vms.size());
//
//        Unsafe unsafe = getUnsafe();
//        long addr = unsafe.allocateMemory(1024);
//
//        System.out.println("addr: " + addr); */
//        try {
//            test();
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    public static Unsafe getUnsafe() {
//        try {
//            Field f = Unsafe.class.getDeclaredField("theUnsafe");
//            f.setAccessible(true);
//            return (Unsafe) f.get(null);
//        } catch (Exception e) {
//            /* ... */
//        }
//
//        return null;
//    }
//
//    static native void test();
//
//    class A {
//
//    }
//}
