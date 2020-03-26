package com.perfma.thread.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import java.util.Collections;

public class StackInfoModel {
    // 序号递增器
    public static AtomicLong                        counter          = new AtomicLong();
    // 序号
    public String                                   sn;
    private String                                  title;
    private Set<ThreadInfoModel>                    inThreads        = new HashSet<ThreadInfoModel>();
    private String                                  clazzmethodline;
    private int                                     threadNum;
    private int                                     runnableThreadNum;
    // 主要辅助load问题分析
    private Set<ThreadInfoModel>                    runnbaleThreads  = new HashSet<ThreadInfoModel>();

    public List<ThreadInfoModel>                    sortedThreads    = new ArrayList<ThreadInfoModel>();

    public Map<HotStackType, List<ThreadInfoModel>> sortedThreadsMap = new HashMap<HotStackType, List<ThreadInfoModel>>();

    public StackInfoModel(String title) {
        this.sn = counter.incrementAndGet() + "";
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThreadNum() {
        threadNum = inThreads.size();
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getRunnableThreadNum() {
        runnableThreadNum = runnbaleThreads.size();
        return runnableThreadNum;
    }

    public void setRunnableThreadNum(int runnableThreadNum) {
        this.runnableThreadNum = runnableThreadNum;
    }

    public void addThreadInfoModel(ThreadInfoModel thread, boolean runnable) {
        this.inThreads.add(thread);
        if (runnable) {
            runnbaleThreads.add(thread);
        }
    }

    public Set<ThreadInfoModel> getInThreads() {
        return inThreads;
    }

    public void setInThreads(Set<ThreadInfoModel> inThreads) {
        this.inThreads = inThreads;
    }

    public String getClazzmethodline() {
        return clazzmethodline;
    }

    public void setClazzmethodline(String clazzmethodline) {
        this.clazzmethodline = clazzmethodline;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public List<ThreadInfoModel> getSortedThreads(HotStackType type) {
        List<ThreadInfoModel> sortedThreads = sortedThreadsMap.get(type);
        if (sortedThreads == null) {
            sortedThreads = new ArrayList<ThreadInfoModel>();

            if (type == HotStackType.hot_stack_all_thread) {
                sortedThreads.addAll(inThreads);
            } else {
                sortedThreads.addAll(runnbaleThreads);
            }

            Collections.sort(sortedThreads, new Comparator<ThreadInfoModel>() {
                @Override
                public int compare(ThreadInfoModel o1, ThreadInfoModel o2) {
                    return o2.getStackLines() - o1.getStackLines();
                }

            });
        }
        return sortedThreads;
    }

    public void setSortedThreads(List<ThreadInfoModel> sortedThreads) {
        this.sortedThreads = sortedThreads;
    }

}
