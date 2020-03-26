package com.proc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/15 18:44
 **/
public class TaskCpuUsage implements Comparable<TaskCpuUsage> {

    public static void main(String[] args) {
        TaskCpuUsage tcu1 = new TaskCpuUsage();
        tcu1.totalCpu = 100;

        TaskCpuUsage tcu2 = new TaskCpuUsage();
        tcu2.totalCpu = 200;

        TaskCpuUsage tcu3 = new TaskCpuUsage();
        tcu3.totalCpu = 150;

        List<TaskCpuUsage> tcuList = new ArrayList<>();
        tcuList.add(tcu1);
        tcuList.add(tcu2);
        tcuList.add(tcu3);

        Collections.sort(tcuList);

        System.out.println("end");
    }

    public double sysCpu;
    public double userCpu;
    public double totalCpu;

    @Override
    public String toString() {
        return "TaskCpuUsage{" +
                "sysCpu=" + sysCpu +
                ", userCpu=" + userCpu +
                ", totalCpu=" + totalCpu +
                '}';
    }

    @Override
    public int compareTo(TaskCpuUsage tcu) {
        return totalCpu == tcu.totalCpu ? 0 : (totalCpu > tcu.totalCpu ? -1 : 1);
    }
}
