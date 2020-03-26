package com.myself;

/**
 * @author xiluo
 * @createTime 2019/1/2 15:10
 **/
public class StringTest {

    public static void main(String[] args) {

        String desc = "线程上下文切换频繁，这个可能导致您系统的负载比较高，相关线程：%s 。";
        desc = String.format(desc,"xiluo");
        System.out.println(desc);

    }
}
