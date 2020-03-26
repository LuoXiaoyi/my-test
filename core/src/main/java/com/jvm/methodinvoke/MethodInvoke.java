package com.jvm.methodinvoke;

/**
 * @author xiaoyiluo
 * @createTime 2018/10/14 14:58
 **/
public class MethodInvoke extends SuperClazz {

    public static void m1(Object obj, Object... others){
        System.out.println("m1 2 invoke");
    }

    public static void m1(String text, Object obj, Object... others){
        System.out.println("m1 3 invoke");
    }

    /*public static void superMethod(){
        System.out.println("MethodInvoke");
    }*/

    public static void main(String[] args) {
        String str = "abc";
        m1(str,null);
        m1(null,"hello","haha");
        m1(null,1);
        m1(null,1,2);
        MethodInvoke.superMethod();
    }
}
