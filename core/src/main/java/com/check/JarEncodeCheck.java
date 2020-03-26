package com.check;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/26 13:03
 * @Version 1.0
 **/
public class JarEncodeCheck {

    public static void main(String[] args) {
        String targetPath = "./";
        if (args.length != 0) {
            targetPath = args[0];
        }

        File dir = new File(targetPath);
        if (dir.isDirectory()) {
            File[] jarFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });

            if (jarFiles != null) {
                for (File jar : jarFiles) {
                    checkAndRecord(jar);
                }
            }
        } else if (targetPath.endsWith(".jar")) {
            checkAndRecord(new File(targetPath));
        } else {
            System.out.println("The input path is not valid directory or .jar file.");
        }
    }

    static void checkAndRecord(File jarFile) {
        try {
            check(jarFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    static void check(File f) throws IOException {
        System.out.println("begin to check: " + f.getAbsolutePath());
        JarFile jarFile = new JarFile(f.getAbsoluteFile());
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entityName = jarEntry.getName();
            byte[] magic = new byte[4];
            if (entityName.endsWith(".class")) {
                InputStream in = jarFile.getInputStream(jarEntry);
                in.read(magic);
                // 大端
                int a = getIntB(magic);
                if (a != 0xcafebabe) {
                    // 小端
                    a = getIntL(magic);
                    if (a != 0xcafababe) {
                        System.err.println("invalid jar file :  " + f.getAbsolutePath() + ", class name: " + entityName);
                    }
                }
                in.close();
            }
        }
    }

    static int getIntB(byte[] magic) {
        return makeInt(magic[0], magic[1], magic[2], magic[3]);
    }

    static int getIntL(byte[] magic) {
        return makeInt(magic[3], magic[2], magic[1], magic[0]);
    }

    static int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) << 8) |
                ((b0 & 0xff)));
    }
}
