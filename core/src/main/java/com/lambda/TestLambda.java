package com.lambda;

/**
 * @author xiluo
 * @createTime 2019/3/2 16:13
 **/
public class TestLambda {

    interface Itf{
        void foo();
    }

    class Caller{

        void call(Itf itf){
            itf.foo();
        }
    }

    public static void main(String[] args) {
        TestLambda lambda = new TestLambda();

        Caller caller = lambda.new Caller();

        caller.call(()->{
            System.out.println("hello lambda...");
        });
    }

    // private static void lambda$main$0(){}
}
