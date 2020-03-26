package com.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author xiaoyiluo
 * @createTime 2018/7/30 22:37
 **/
public class MySecurityManager extends SecurityManager {


    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());

        try {
            FileInputStream fis = new FileInputStream(new File("hello.test"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkRead(String file){
        if(file.endsWith(".test"))
            throw new SecurityException("你没有读取的本文件的权限");
    }
}
