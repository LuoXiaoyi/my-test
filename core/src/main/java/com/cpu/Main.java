package com.cpu;

import java.util.*;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-26 15:39
 **/
public class Main {

    static long samples1 = 1;
    static long samples2 = 2;
    static long samples3 = 3;
    static long samples4 = 4;
    static long samples5 = 5;

    static long[] mids1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static long[] mids2 = {1, 2, 3, 4, 55, 66, 77, 88, 9};
    static long[] mids3 = {1, 2, 33, 4, 55, 66, 777, 888, 9};
    static long[] mids4 = {1, 21, 3, 4, 5, 6, 7, 80};
    static long[] mids5 = {0, 2, 3, 4, 5, 6, 7, 80};

    public static void main(String[] args) {

        List<StackNode> mergedStacks = getStackNodes();
        /*List<StackNode> mergedStacks2 = getStackNodes();

        List<StackNode> msns = new ArrayList<>(2);
        msns.add(mergedStacks.get(0));
        msns.add(mergedStacks2.get(0));

        List<StackNode> mergedStacks3 = MergeUtil.mergeStackNodes(msns);
        printInfo(mergedStacks3);*/
    }

    private static List<StackNode> getStackNodes() {
        StackNode sn11 = generateStackNode(samples1, mids1);
        StackNode sn22 = generateStackNode(samples2, mids2);
        StackNode sn33 = generateStackNode(samples3, mids3);
        StackNode sn44 = generateStackNode(samples4, mids4);
        StackNode sn55 = generateStackNode(samples5, mids5);

        List<StackNode> sns = new LinkedList<>();
        sns.add(sn11);
        sns.add(sn22);
        sns.add(sn33);
        sns.add(sn44);
        sns.add(sn55);

        List<StackNode> mergedStacks2 = MergeUtil.mergeStackNodes(sns);

        printInfo(mergedStacks2);

        return mergedStacks2;
    }

    private static void printInfo(List<StackNode> mergedStacks2) {
        for (StackNode sn : mergedStacks2) {
            List<FrameBean> fbs = sn.queryAllStackFramesAsRootStackNode();
            System.out.println("total size: " + fbs.size());
            for (FrameBean fb : fbs) {
                if(fb.getParentSlotId() == null){
                    System.out.println(fb);
                }
            }
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
            for (FrameBean fb : fbs) {
                if(fb.getParentSlotId() != null){
                    System.out.println(fb);
                }
            }
        }
    }

    static StackNode generateStackNode(long samples, long[] frameIds) {
        StackNode sn = new StackNode();
        sn.setSamples(samples);
        for (int i = 0; i < frameIds.length; ++i) {
            sn.add(new FrameNode(frameIds[i], samples));
        }

        return sn;
    }
}
