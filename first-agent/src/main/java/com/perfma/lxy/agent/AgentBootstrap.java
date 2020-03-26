package com.perfma.lxy.agent;

import com.ibm.jvm.Dump;
import com.ibm.jvm.InvalidDumpOptionException;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiluo
 * @createTime 2019/1/19 23:24
 **/
public class AgentBootstrap {
    static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("on load premain invoked. args: " + args);
        addTransformers(instrumentation, "true".equals(args));
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("on attach agentmain invoked. args: " + args);
        //addTransformers(instrumentation, "true".equals(args));
        try {
            triggerDumpRequest(0, "/home/xiluo/test/0001/001.txt");
            triggerDumpRequest(1, "/home/xiluo/test/0001/002.phd");
        } catch (InvalidDumpOptionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param type 0 thread
     *             1 memory
     * @return void
     * @Description TODO
     * 2020/1/19 16:33
     * @author xiluo
     */
    static void triggerDumpRequest(int type, String filePath) throws InvalidDumpOptionException {
        switch (type) {
            case 0:
                Dump.javaDumpToFile(filePath);
                break;
            case 1:
                Dump.heapDumpToFile(filePath);
                break;
            default:
        }
    }

    static void addTransformers(Instrumentation instrumentation, final boolean flag) {
        System.out.println("instrumentation: " + instrumentation);
        AgentBootstrap.instrumentation = instrumentation;
        if (instrumentation.isNativeMethodPrefixSupported()) {
            System.out.println("isNativeMethodPrefixSupported");
        } else {
            System.out.println("Not isNativeMethodPrefixSupported");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("add " + ClassLoggerTransformer.class + ", canRetransform: " + flag);
                //instrumentation.addTransformer(new TimeRecorderTransformer(), false);
                //System.out.println("add " + TimeRecorderTransformer.class);
                ClassFileTransformer transformer = new ClassLoggerTransformer();
                AgentBootstrap.instrumentation.addTransformer(transformer, flag);
                File f = null;
                try {
                    Class[] toBeRetransformedClasses = getAllReallyToBeTransformedClasses();
                    if (toBeRetransformedClasses != null) {
                        System.out.println("toBeRetransformedClasses: " + Arrays.asList(toBeRetransformedClasses));
                        ClassDefinition cd = new ClassDefinition(File.class, new byte[100]);
                        AgentBootstrap.instrumentation.redefineClasses(cd);
                        //AgentBootstrap.instrumentation.retransformClasses(toBeRetransformedClasses);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    AgentBootstrap.instrumentation.removeTransformer(transformer);
                }
            }
        }).start();
    }

    static Class[] getAllReallyToBeTransformedClasses() {
        Class[] srcClasses = instrumentation.getAllLoadedClasses();
        List<Class> reallyToBeTransformed = new LinkedList<Class>();
        for (Class<?> cls : srcClasses) {
            if (ClassesFilter.ignoreTypeOf(cls) != IgnoreType.NONE) {
                continue;
            }
            reallyToBeTransformed.add(cls);
        }

        if (reallyToBeTransformed.isEmpty()) {
            return null;
        }

        // 构建真正需要被用于增强的类
        Class[] classArray = new Class[reallyToBeTransformed.size()];
        int i = 0;
        for (Class cls : reallyToBeTransformed) {
            classArray[i++] = cls;
        }

        return classArray;
    }
}
