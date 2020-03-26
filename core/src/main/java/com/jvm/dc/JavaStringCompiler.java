package com.jvm.dc;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * test
 *
 * @author xiluo
 * @createTime 2020-01-10 19:31
 **/
public class JavaStringCompiler {
    private JavaCompiler compiler;
    private StandardJavaFileManager stdManager;
    private Map<ClassLoader, PackageInternalsFinder> finderMap;

    public JavaStringCompiler() {
        compiler = ToolProvider.getSystemJavaCompiler();
        stdManager = compiler.getStandardFileManager(null, null, null);
        finderMap = new ConcurrentHashMap<>();
    }

    public Class<?> loadClass(String className, String javaSourceCode) throws ClassNotFoundException, IOException {
        MemoryJavaFileManager fileManager = getMemoryJavaFileManager();
        try {
            String[] names = className.split("\\.");
            JavaFileObject javaFileObject = fileManager.makeStringSource(names[names.length - 1], javaSourceCode);
            List<String> compileOptions = new ArrayList<>(Arrays.asList("-classpath", System.getProperty("java.class.path")));
            compileOptions.add("-XDuseUnSharedTable");
            compileOptions.add("-XDuseJavaUtilZip");

            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                    null, compileOptions, null, Arrays.asList(javaFileObject));

            Boolean result = task.call();
            if (result == null || !result) {
                throw new RuntimeException("Compilation class: " + className + " error.");
            }

            Map<String, byte[]> classBytes = fileManager.getClassBytes();
            MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
            return  classLoader.loadClass(className);
        } finally {
            fileManager.close();
        }
    }

    private MemoryJavaFileManager getMemoryJavaFileManager() {
        synchronized (finderMap) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            PackageInternalsFinder finder = finderMap.get(classLoader);
            if (finder == null) {
                finder = new PackageInternalsFinder(classLoader);
                finderMap.put(classLoader, finder);
            }

            return new MemoryJavaFileManager(stdManager, classLoader, finder);
        }
    }
}
