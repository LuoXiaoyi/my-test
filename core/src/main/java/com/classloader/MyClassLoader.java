package com.classloader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xiaoyiluo
 * @createTime 2018/7/29 21:59
 **/
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {
        System.out.println("getSecurityManager: " + System.getSecurityManager());

        String classBaseDir = "/Users/xiaoyiluo/Documents/my-space/code/jvm-deep/l3////";
        MyClassLoader classLoader = new MyClassLoader(classBaseDir);

        String className = "Singleton";
        try {
            Class<?> clazz = classLoader.loadClass(className);
            System.out.println("success in loading class : " + clazz.getClass());

            Method getInstance = clazz.getDeclaredMethod("getInstance",boolean.class);
            if(getInstance != null){
                getInstance.invoke(null,true);
                System.out.println("=========================");
                getInstance.invoke(null,false);
            }

            Method mainMethod = clazz.getDeclaredMethod("main",String[].class);
            System.out.println(" begin to invoke main: ");
            if(mainMethod != null){
                /**
                 *  mainMethod.invoke(null, args);
                 *
                 *  直接这样写会报错，因为，如果我们直接传入的是 args 数组，那么，根据 Object...的定义，会直接把 args 数组拆开传过去
                 *  这样 main 方法就会收到多个参数，而实际上它所需要的参数为 String[] 数组
                 */
                mainMethod.invoke(null,(Object)args);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String classBaseDir;

    public MyClassLoader(String classBaseDir) {
        this.classBaseDir = classBaseDir;
        while (this.classBaseDir.endsWith("/")) {
            this.classBaseDir = this.classBaseDir.substring(0, this.classBaseDir.lastIndexOf("/"));
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        byte[] classBytes = loadClassBytes(name);

        if(classBytes != null){
            return this.defineClass(name,classBytes,0,classBytes.length);
        }

        return null;
    }

    private byte[] loadClassBytes(String name) {

        String className = name.replace(".", "/");
        String fullClassPath = classBaseDir + File.separator + className + ".class";
        System.out.println("fullClassPath -> " + fullClassPath);

        File classFile = new File(fullClassPath);
        if (!classFile.exists()) {
            System.err.println(fullClassPath + " not found.");
            return null;
        }
        ByteArrayOutputStream baos = null;
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(classFile));
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int readCnt = -1;
            while ((readCnt = bis.read(buf)) != -1) {
                baos.write(buf, 0, readCnt);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return baos == null ? null : baos.toByteArray();
    }
}
