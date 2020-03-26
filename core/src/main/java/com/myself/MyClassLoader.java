package com.myself;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author xiluo
 * @createTime 2019/1/21 18:15
 **/
public class MyClassLoader extends ClassLoader {
    String classFilePath;

    public MyClassLoader(String classFilePath) {
        this.classFilePath = classFilePath;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clz = findClass(name);
        if (clz == null) return super.loadClass(name, resolve);
        return clz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("load class: " + name);
        if (name.endsWith("Test")) {
            try {
                File f = new File(this.classFilePath + "/" + name + ".class");
                System.out.println("abs path: " + f.getAbsolutePath());
                int size = (int) f.length();
                byte[] bytes = new byte[size];
                BufferedInputStream bais = new BufferedInputStream(new FileInputStream(f));
                int cnt = bais.read(bytes);
                System.out.println("readcnt: " + cnt + ", size: " + size);
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                throw new ClassNotFoundException(e.getMessage());
            }
        }

        return null;
    }
}
