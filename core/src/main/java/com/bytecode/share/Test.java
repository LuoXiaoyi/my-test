package com.bytecode.share;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-23 13:58
 **/
public class Test {

    public static void execute(){
        long sum = 0;
        for (int i = 0;i < Integer.MAX_VALUE; ++i){
            sum +=i;
        }

        System.out.println("sum: " + sum);
    }

}
