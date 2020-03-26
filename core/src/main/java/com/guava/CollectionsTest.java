package com.guava;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/8 14:20
 **/
public class CollectionsTest {

    public static void main(String[] args) {

        List<Integer> list = Lists.newArrayList(1,2,34,5,6,534434,2323);
        String string = CharMatcher.DIGIT.retainFrom("some text89983and more");
        System.out.println("digit: "  + string);
        ArrayListMultimap.create();
    }
}
