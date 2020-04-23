package com.arithmetic;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/18 17:21
 * @Version 1.0
 **/
public class BubbleSort {

    static void sort(int eles[]) {
        for (int i = eles.length - 1; i > -1; --i) {
            for (int j = i - 1; j > -1; --j) {
                if(eles[i] < eles[j]){
                    int t =  eles[i];
                    eles[i] = eles[j];
                    eles[j] = t;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] eles = {34,5,6,7,21,34,3,67,8,9,2,1,5,7,9,0,103};
        sort(eles);
        for (int i: eles){
            System.out.print(i + " ");
        }
    }
}
