package com.mxbean;

import sun.management.VMManagement;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/17 13:42
 * @Version 1.0
 **/
public class MXBeanTest {
    public static void main(String[] args) {
        new Thread(() -> {
            Map<String, byte[]> cache = new HashMap<>();
            final String file = "/Users/xiaoyiluo/Documents/my-space/code/test/core/src/main/java/com/mxbean/MXBeanTest.java";
            long idx = 0;
            while (true) {
                try (BufferedInputStream bios = new BufferedInputStream(new FileInputStream(file))) {
                    byte[] a = new byte[1024 * 1024];
                    int readCnt = 0;
                    while ((readCnt = bios.read(a)) > 0) {
                        cache.put("key->" + idx++, a);
                    }
                    if (idx > 15000) {
                        idx = 0;
                        //cache.clear();
                        //cache = new HashMap<>();
                    }
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                gcMxBeanTest();
                /*heapMxBeanTest();
                threadMxBeanTest();
                runtimeMxBeanTest();
                memoryMxBeanTest();
                operatingSystemTest();*/
                System.out.println("################################################");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    static void operatingSystemTest() {
        System.out.println("Operating System   ++++++++++++++++++++++++++++++++++++++");
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        System.out.printf(" name: %s, version: %s, arch: %s, cpu cores: %d, sys load: %f. \n",
                os.getName(), os.getVersion(), os.getArch(), os.getAvailableProcessors(), os.getSystemLoadAverage());

        try {
            Class<?> osi = Class.forName("sun.management.OperatingSystemImpl");
            if (osi.isAssignableFrom(os.getClass())) {
                System.out.println("getTotalSwapSpaceSize: " + invokeMethod(osi, os, "getTotalSwapSpaceSize"));
                System.out.println("getFreeSwapSpaceSize: " + invokeMethod(osi, os, "getFreeSwapSpaceSize"));
                System.out.println("getProcessCpuTime: " + invokeMethod(osi, os, "getProcessCpuTime") + " ns");
                System.out.println("getFreePhysicalMemorySize: " + invokeMethod(osi, os, "getFreePhysicalMemorySize"));
                System.out.println("getTotalPhysicalMemorySize: " + invokeMethod(osi, os, "getTotalPhysicalMemorySize"));
                System.out.println("getOpenFileDescriptorCount: " + invokeMethod(osi, os, "getOpenFileDescriptorCount"));
                System.out.println("getMaxFileDescriptorCount: " + invokeMethod(osi, os, "getMaxFileDescriptorCount"));
                System.out.println("getSystemCpuLoad: " + invokeMethod(osi, os, "getSystemCpuLoad"));
                System.out.println("getProcessCpuLoad: " + invokeMethod(osi, os, "getProcessCpuLoad"));

                Field f = osi.getSuperclass().getDeclaredField("jvm");
                f.setAccessible(true);
                Object obj = f.get(os);
                Class<?> vmClazz = obj.getClass();
                if (VMManagement.class.isAssignableFrom(vmClazz)) {
                    System.out.println("getSafepointCount: " + invokeMethod(vmClazz, obj, "getSafepointCount"));
                    System.out.println("getTotalSafepointTime: " + invokeMethod(vmClazz, obj, "getTotalSafepointTime"));
                    System.out.println("getLoadedClassSize: " + invokeMethod(vmClazz, obj, "getLoadedClassSize"));
                    System.out.println("getClassLoadingTime: " + invokeMethod(vmClazz, obj, "getClassLoadingTime"));
                    System.out.println("getTotalClassCount: " + invokeMethod(vmClazz, obj, "getTotalClassCount"));
                    System.out.println("getUnloadedClassCount: " + invokeMethod(vmClazz, obj, "getUnloadedClassCount"));
                    System.out.println("getTotalCompileTime: " + invokeMethod(vmClazz, obj, "getTotalCompileTime"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void memoryMxBeanTest() {
        System.out.println("Memory   ++++++++++++++++++++++++++++++++++++++");
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.printf("Heap usage: %s \n", memoryMXBean.getHeapMemoryUsage().toString());
        System.out.printf("Non heap usage: %s \n", memoryMXBean.getNonHeapMemoryUsage().toString());
    }

    static void runtimeMxBeanTest() {
        System.out.println("Runtime   ++++++++++++++++++++++++++++++++++++++");
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        // 系统属性 + 启动参数
        System.out.printf(" machine name: %s \n System props: %s \n Jvm options: %s \n", runtimeMXBean.getName(),
                runtimeMXBean.getSystemProperties().toString(),
                runtimeMXBean.getInputArguments().toString());

        // path 信息
        System.out.printf(" lib path: %s \n class path: %s \n boot class path: %s \n",
                runtimeMXBean.getLibraryPath(), runtimeMXBean.getClassPath(), runtimeMXBean.getBootClassPath());

        // VM info
        System.out.printf(" VM vendor: %s, VM name: %s, VM version: %s \n", runtimeMXBean.getVmVendor(),
                runtimeMXBean.getVmName(), runtimeMXBean.getVmVersion());

        // Java specification info
        System.out.printf(" spec vendor: %s, spec name: %s, spec version: %s \n", runtimeMXBean.getSpecVendor(),
                runtimeMXBean.getSpecName(), runtimeMXBean.getSpecVersion());

        // start time
        System.out.printf(" start time: %s, up time: %d \n", new Date(runtimeMXBean.getStartTime()).toString(),
                runtimeMXBean.getUptime());
    }

    static void threadMxBeanTest() {
        System.out.println("Thread   ++++++++++++++++++++++++++++++++++++++");
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadLocks = threadMXBean.findDeadlockedThreads();
        long[] monitorDeadLocks = threadMXBean.findMonitorDeadlockedThreads();
        System.out.printf(" Total started: %d, peak: %d, current: %d, daemon: %d, dead lock: %d, monitor dead lock: %d.\n",
                threadMXBean.getTotalStartedThreadCount(), threadMXBean.getPeakThreadCount(), threadMXBean.getThreadCount(),
                threadMXBean.getDaemonThreadCount(), deadLocks == null ? 0 : deadLocks.length,
                monitorDeadLocks == null ? 0 : monitorDeadLocks.length);
    }

    /**
     * 默认 GC 算法是：PS Scavenge + PS MarkSweep
     * 设置 GC：-XX:+UseConcMarkSweepGC / -XX:+UseG1GC
     * <p>
     * -Xmx64M
     * -verbose:gc -XX:+PrintGCDetails
     */
    static void heapMxBeanTest() {
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        System.out.println("Memory   ++++++++++++++++++++++++++++++++++++++");
        for (MemoryPoolMXBean mxBean : memoryPoolMXBeans) {
            System.out.printf("%s: \n name[%s] \n peak usage:[%s] \n current usage: [%s] \n usage threshold: [%d], " +
                            "Usage threshold count: %d \n collection usage: [%s] \n collection Usage threshold: [%d]" +
                            " collection usage threshold count: [%d]\n",
                    mxBean.getType(), mxBean.getName(), mxBean.getPeakUsage(), mxBean.getUsage(),
                    mxBean.isUsageThresholdSupported() ? mxBean.getUsageThreshold() : 0,
                    mxBean.isUsageThresholdSupported() ? mxBean.getUsageThresholdCount() : 0,
                    mxBean.getCollectionUsage(),
                    mxBean.isCollectionUsageThresholdSupported() ? mxBean.getCollectionUsageThreshold() : 0,
                    mxBean.isCollectionUsageThresholdSupported() ? mxBean.getCollectionUsageThresholdCount() : 0);
        }
        System.out.println();
    }

    /**
     * 默认 GC 算法是：PS Scavenge + PS MarkSweep
     * 设置 GC：-XX:+UseConcMarkSweepGC / -XX:+UseG1GC
     * <p>
     * -Xmx64M
     * -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps
     * -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime
     * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/xiaoyiluo/Documents/my-space/code/test
     */
    static void gcMxBeanTest() {
        List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        System.out.println("GC   ++++++++++++++++++++++++++++++++++++++");
        for (GarbageCollectorMXBean gcBean : gcMxBeans) {
            System.out.printf("GC name: %s, total count: %s, total cost: %d ms. \n",
                    gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionTime());

            if (gcBean.getMemoryPoolNames() != null) {
                System.out.print("Memory Pool Names: ");
                for (String name : gcBean.getMemoryPoolNames()) {
                    System.out.print(name + "\t");
                }
            }

            // get GCInfo
            if (gcBean instanceof com.sun.management.GarbageCollectorMXBean) {
                com.sun.management.GarbageCollectorMXBean sunGcBean
                        = com.sun.management.GarbageCollectorMXBean.class.cast(gcBean);
                System.out.println("GcInfo: " + sunGcBean.getLastGcInfo());
            }

            System.out.println();
        }
    }

    static Object invokeMethod(Class<?> clzz, Object instance, String methodName, Object... args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = clzz.getDeclaredMethod(methodName);
        if (!m.isAccessible()) {
            m.setAccessible(true);
        }

        return m.invoke(instance, args);
    }
}
