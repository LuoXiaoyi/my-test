package com.jvm.dc;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;

/**
 * test
 *
 * @author xiluo
 * @createTime 2020-01-10 19:33
 **/
public class PackageInternalsFinder {
    private static final String CLASS_FILE_EXTENSION = ".class";
    private final ClassLoader classLoader;
    private final Map<String, List<JavaFileObject>> packageFileObject;
    private final Map<String, Object> parallelLockMap;

    public PackageInternalsFinder(ClassLoader classLoader) {
        this.classLoader = classLoader;
        packageFileObject = new ConcurrentHashMap<>();
        parallelLockMap = new ConcurrentHashMap<>();
    }

    public Iterable<JavaFileObject> find(String packageName) throws IOException {
        synchronized (getPackageLock(packageName)) {
            List<JavaFileObject> javaFileObjects = packageFileObject.get(packageName);
            if (javaFileObjects != null) {
                return javaFileObjects;
            }

            String javaPkgName = packageName.replaceAll("\\.", "/");
            List<JavaFileObject> result = new LinkedList<>();
            Enumeration<URL> urlEnumeration = classLoader.getResources(javaPkgName);
            while (urlEnumeration.hasMoreElements()) {
                result.addAll(listUnder(packageName, urlEnumeration.nextElement()));
            }

            return result;
        }
    }

    private Object getPackageLock(String pkgName) {
        Object lock = parallelLockMap.get(pkgName);
        if (lock == null) {
            synchronized (parallelLockMap) {
                lock = parallelLockMap.get(pkgName);
                if (lock == null) {
                    lock = new Object();
                    parallelLockMap.put(pkgName, lock);
                }
            }
        }

        return lock;
    }

    private Collection<JavaFileObject> listUnder(String pkgName, URL pkgUrl) {
        File dir = new File(pkgUrl.getFile());
        if (dir.isDirectory()) {
            return processDir(pkgName, dir);
        } else {
            return processJar(pkgUrl);
        }
    }

    private Collection<JavaFileObject> processJar(URL pkgUrl) {
        List<JavaFileObject> result = new LinkedList<>();
        try {
            String jarUri = pkgUrl.toExternalForm();
            JarURLConnection jarURLConnection = (JarURLConnection) pkgUrl.openConnection();
            String rootEntryName = jarURLConnection.getEntryName();
            int rootEnd = rootEntryName.length() + 1;
            jarUri = jarUri.substring(0, jarUri.length() - rootEnd + 1);

            Enumeration<JarEntry> entryEnumeration = jarURLConnection.getJarFile().entries();

            while (entryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = entryEnumeration.nextElement();
                String name = jarEntry.getName();

                if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1
                        && name.endsWith(CLASS_FILE_EXTENSION)) {
                    URI uri = URI.create(jarUri.replaceAll("classes!", "classes") + name);

                    String binaryName = name.replaceAll("/", ".");
                    binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                    result.add(new CustomJavaFileObject(binaryName, uri));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Wasn't able to opne " + pkgUrl + " as a jar file", e);
        }
        return result;
    }

    private Collection<JavaFileObject> processDir(String pkgName, File dir) {
        List<JavaFileObject> result = null;
        File[] childrenFiles = dir.listFiles();
        if (childrenFiles != null) {
            result = new LinkedList<>();
            for (File childFile : childrenFiles) {
                if (childFile.isFile()) {
                    if (childFile.getName().endsWith(CLASS_FILE_EXTENSION)) {
                        String binName = pkgName + "." + childFile.getName();
                        binName = binName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                        result.add(new CustomJavaFileObject(binName, childFile.toURI()));
                    }
                }
            }
        }
        return result;
    }
}
