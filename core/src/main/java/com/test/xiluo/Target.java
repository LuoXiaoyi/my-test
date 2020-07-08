package com.test.xiluo;

import com.perfma.toolkit.common.util.FileUtil;

import java.io.File;

/**
 * @author xiluo
 * @ClassName
 * @Description TODO
 * @Date 2020/2/13 15:58
 * @Version 1.0
 **/
public class Target {

    public static void main(String[] args) throws InterruptedException {



       /* while (true){
            System.out.println("hello world");
            Thread.sleep(1000);

            sum(1,3);
        }
*/
       test();
       System.out.println(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile());
       System.out.println(String.class.getProtectionDomain().getCodeSource().getLocation().getFile());
    }

    static void test(){
        String home = "/Users/xiaoyiluo/Documents/my-space/code/test/perfma-wizard-core-2.5.1-RELEASE.jar";
        File coreFile = new File(home);
        if(coreFile.exists()){
           for(File f: coreFile.getParentFile().listFiles()){
               if(f.getName().contains("agent") && f.getName().endsWith(".jar")){
                   System.out.println(f.getName());
                   System.out.println(f.getAbsoluteFile());
                   System.out.println(f);
               }
           }
        }
    }

    static int sum(int a, int b){
        return a + b;
    }
}
