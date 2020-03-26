package com.myself;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaoyiluo
 * @createTime 04/06/2018 16:28
 **/
public class CircleCheck {

    public static void main(String[] args) {
        List<Integer> circle1 = Arrays.asList(1,2,3,4,1);
        List<Integer> circle2 = Arrays.asList(3,4,1,2,3);

        if(areTheySameCircles(circle1,circle2)){
            System.out.println("is the same circle...");
        }
    }

    private static boolean areTheySameCircles(List<?> circle1, List<?> circle2) {

        assert circle1.size() == circle2.size();
        assert circle1.size() >= 2;
        assert circle2.size() >= 2;
        assert circle1.get(0) == circle1.get(circle1.size()-1);
        assert circle2.get(0) == circle2.get(circle2.size()-1);

        Object compareStartObj = null;
        boolean mark = false, isEqual = false;
        int length = circle1.size();
        int k = 0;
        int i = 0, j = 0;

        while (true) {
            if (i == length - 1) break;

            compareStartObj = circle1.get(i);

            if (mark) {
                if (j >= length - 1) {
                    j = 0;
                }

                if (!compareStartObj.equals(circle2.get(j))) {
                    isEqual = false;
                    break;
                }

                ++i;
                ++j;
            } else {
                if (compareStartObj.equals(circle2.get(j))) {
                    ++i;
                    mark = true;
                }

                if (j == length - 1) {
                    break;
                }

                ++j;
            }
        }

        if (i == length - 1) {
            isEqual = true;
        }

        return isEqual;
    }

}
