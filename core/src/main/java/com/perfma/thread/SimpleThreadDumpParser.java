package com.perfma.thread;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/16 10:01
 **/
public class SimpleThreadDumpParser {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "/Users/xiaoyiluo/Desktop/Jstack-l/twoDeadLock";
        List<ValuesGroup> vgs = doParse(path);
        System.out.println(vgs);
        /*
        int a[] = {2, 4, 5, 6, 7, 8};
        int b[] = {1, 2, 3, 4, 5, 7};
        match(a, b);*/
    }

    private static void match(int[] a, int b[]) {
        int jstackIdx = 0, jstackLen = a.length;
        int mxBeanIdx = 0, mxBeanLen = b.length;
        int mxBeanBeginIdxNextTurn = 0;
        // previous threads
        while (jstackIdx < jstackLen) {
            int lastJstackIdx = jstackIdx;
            // latest threads，从上一次匹配上的元素的下一个元素开始中一轮的匹配
            mxBeanIdx = mxBeanBeginIdxNextTurn;
            while (mxBeanIdx < mxBeanLen) {
                //ValuesGroup jstack = jstackDumpThreadInfos.get(jstackIdx);
                // 如果都匹配到了，则都往下一个元素进一步，然后更新下一轮匹配开始的位置
                //if (mxBeanDumpThreadsInfo[mxBeanIdx].getThreadName().equals(jstack.threadName)) {
                if (a[jstackIdx] == b[mxBeanIdx]) {
                    System.out.println(a[jstackIdx]);
                    mxBeanIdx++;
                    jstackIdx++;
                    mxBeanBeginIdxNextTurn = mxBeanIdx;
                    // pid2Tid.put(jstack.twpId, mxBeanDumpThreadsInfo[mxBeanIdx].getThreadId());
                } else {
                    // 可能是新增的线程，那么会在注册的线程创建事件中被捕捉到，后续会去更新相关的缓存
                    mxBeanIdx++;
                }
            }

            // 整个最新dump出来的线程中都没有找到，说明该老的线程已经被删除掉了，不要关注该线程，则跳过该线程，开始下一个线程的匹配
            if (lastJstackIdx == jstackIdx) {
                jstackIdx++;
            }
        }
    }

    /**
     * 只解释<threadName, tid, tpwId> 这个三元组，用于做线程的映射
     * @param threadDumpFilePath 线程 dump 文件
     * @return 返回解释的结果
     * @throws FileNotFoundException
     */
    public static List<ValuesGroup> doParse(String threadDumpFilePath) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(threadDumpFilePath));
        List<ValuesGroup> vgs = new LinkedList<>();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                if(line.startsWith("\"")) {
                    ValuesGroup vg = new ValuesGroup();
                    vg.threadName = line.substring(1, line.lastIndexOf("\""));
                    line = line.substring(line.lastIndexOf("\"") + 1).trim();
                    String[] threadFields = line.split(" ");
                    for (int i = 0; i < threadFields.length; ++i) {
                        if (threadFields[i].startsWith("#")) {
                            vg.tid = Long.valueOf(threadFields[i].substring(1));
                        } else if (threadFields[i].startsWith("nid=0x")) {
                            vg.twpId = Long.parseLong(threadFields[i].substring(6), 16);
                        }
                    }

                    vgs.add(vg);
                }else if(line.startsWith("JNI global references")){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return vgs;
    }

    private static class ValuesGroup {
        String threadName;
        long tid = -1;
        long twpId;

        @Override
        public String toString() {
            return "ValuesGroup{" +
                    "threadName='" + threadName + '\'' +
                    ", tid=" + tid +
                    ", twpId=" + twpId +
                    '}';
        }
    }
}
