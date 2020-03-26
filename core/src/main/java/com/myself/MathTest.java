package com.myself;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xiluo
 * @createTime 2019-04-19 14:23
 **/
public class MathTest {
    private static Random random = new Random();
    public int illegalArgumentCount = 0;

    public static void main(String[] args) throws InterruptedException {
        final MathTest game = new MathTest();


        new Thread(() -> {
            try {
                while (true) {
                    game.run();
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (Throwable t) {
            }
        }).start();

        while (true) {
            game.run();
            TimeUnit.SECONDS.sleep(1);
        }

    }

    public List<Integer> addOne(List<Integer> primeFactors) {
        if (primeFactors != null) {
            System.out.println("list size: " + primeFactors.size());
            return primeFactors.stream().map(ele -> ele++).collect(Collectors.toList());
        }

        return null;
    }

    public void run() throws InterruptedException {
        try {
            int number = random.nextInt() / 10000;
            testRealObj();
            List<Integer> primeFactors = primeFactors2(number);
            primeFactors = addOne(primeFactors);
            primeFactors(5, false);
            print(number, primeFactors);

            for (int i = 0; i < 1000; ++i)
                primeFactors(5, false);

            primeFactors(5, false);
            primeFactors(5, false);

            primeFactors(5, false);
        } catch (Exception e) {
            System.out.println(String.format("illegalArgumentCount:%3d, ", illegalArgumentCount) + e.getMessage());
        }
    }

    public static void print(int number, List<Integer> primeFactors) {
        System.out.println("list size: " + primeFactors.size());
        StringBuffer sb = new StringBuffer(number + "=");
        for (int factor : primeFactors) {
            sb.append(factor).append('*');
        }
        if (sb.charAt(sb.length() - 1) == '*') {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
    }

    public List<Integer> primeFactors2(int number) {
        if (number < 2) {
            illegalArgumentCount++;
            throw new IllegalArgumentException("number is: " + number + ", need >= 2");
        }

        List<Integer> result = new ArrayList<Integer>();
        int i = 2;
        while (i <= number) {
            if (number % i == 0) {
                result.add(i);
                number = number / i;
                i = 2;
            } else {
                i++;
            }
        }

        return result;
    }

    // 测试重载方法
    public void primeFactors(int dp, boolean f) {
        Random r = new Random();
        r.nextInt(10000);
        if (dp == 0) return;
        primeFactors(dp - 1, f);
    }

    interface Itf {
        int itf(String n);
    }

    class A implements Itf {
        public int itf(String n) {
            System.out.println("a.itf()->" + n);
            return 0;
        }

        void ok(String n) {
            System.out.println("a.ok()->" + n);
        }
    }

    class B extends A {
        void ok(String n) {
            System.out.println("b.ok()->" + n);
        }


        public int itf(String n) {
            System.out.println("b.itf()->" + n);
            System.currentTimeMillis();
            return 0;
        }
    }

    public void testRealObj() {
        Itf i = new A();
        String b = "abc";
        i.itf(b);

        A a = new B();
        b = "efg";
        a.ok(b);
        a.itf("b.itf");
    }
}
