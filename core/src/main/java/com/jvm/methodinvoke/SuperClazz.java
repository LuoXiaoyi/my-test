package com.jvm.methodinvoke;

/**
 * @author xiaoyiluo
 * @createTime 2018/10/14 20:35
 **/
public class SuperClazz {

    public static void superMethod(){
        System.out.println("SuperClazz");
    }

    public static final void superMethod2(){
        System.out.println("SuperClazz.superMethod2 is final, can not be over writer.");
    }
}
