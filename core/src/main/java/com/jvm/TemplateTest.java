package com.jvm;

import java.util.Collection;
import java.util.List;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/1 12:38
 * @Version 1.0
 **/
public class TemplateTest {

    public static void main(String[] args) {
        List<Aa> as = null;
        test(as);
        test2(as);

        List<Bb> bs = null;
        test(bs);
        test2(bs);
    }

    static void test(Collection<? extends Aa> as) {

    }

    static void test2(Collection<? super Bb> as) {

    }

    static class Aa {
        int a;
    }

    static class Bb extends Aa {
        int b;
    }
}
