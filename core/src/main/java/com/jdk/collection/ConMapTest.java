package com.jdk.collection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/19 15:38
 * @Version 1.0
 **/
public class ConMapTest {

    static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        testMap();
        for (int i = 0; i < 10; ++i) {
            System.out.println(i + ": " + poisson(i));
        }

        // Map 的最大容量 00000000 00000000 00000000 00000001 -> 01000000 00000000 00000000 00000000
        System.out.println(1 << 30);
        // 10000000 00000000 00000000 00000001 -> 11111111 11111111 11111111 11111111 -> 00111111 11111111 11111111 11111111
        System.out.println(-1 >>> 2);
        // 10000000 00000000 00000000 00000001 -> 11111111 11111111 11111111 11111111 -> 11111111 11111111 11111111 11111111
        System.out.println(-1 >> 30);
    }

    static void testMap() {
        map.put("xiluo", "abc");
        map.put("xiluo2", "abc");
        map.put("xiluo3", "abc");
        map.put("xiluo4", "abc");
        map.put("xiluo5", "abc");
        map.put("xiluo6", "abc");

        map.computeIfAbsent("nora", (key) -> {
            return key + "heihei";
        });

        map.computeIfPresent("nora", (key, value) -> {
            return key + value + "-aa";
        });

        map.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
    }

    /**
     * 泊松分布概率计算公式，其中 m = 0.5
     * (exp(-0.5) * pow(0.5, k) / factorial(k))
     */
    static double poisson(int k) {
        return Math.exp(-0.5) * Math.pow(0.5, k) / factorial(k);
    }

    static long factorial(int k) {
        if (k == 0) return 1;
        long total = 1;
        for (int i = 2; i <= k; ++i) {
            total *= i;
        }
        return total;
    }
}
