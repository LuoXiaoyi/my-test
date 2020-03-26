package com.myself;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/21 14:11
 **/
public class TestSort implements Comparable<TestSort> {

    int id;

    @Override
    public String toString() {
        return "id: " + id;
    }

    public int compareTo(TestSort o) {
        return o.id > id ? -1 : o.id == id ? 0 : 1;
    }

    public static void main(String[] args) {
        String p = "MathTest$A";
        boolean b = p.matches("com.myself.MathTest$A");
        System.out.println(b);

        List<TestSort> testSorts = new LinkedList<>();
        TestSort ts = new TestSort();
        ts.id = 10;
        testSorts.add(ts);

        ts = new TestSort();
        ts.id = 40;
        testSorts.add(ts);

        ts = new TestSort();
        ts.id = 19;
        testSorts.add(ts);

        Collections.sort(testSorts);

        System.out.println(testSorts);
    }
}
