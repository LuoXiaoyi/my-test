package com.bytecode.newi;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-10 10:34
 **/
public class NativeTest {

    public native void test1();
}

class A extends NativeTest{

    @Override
    public void test1() {
        System.out.println("abc");
    }
}
