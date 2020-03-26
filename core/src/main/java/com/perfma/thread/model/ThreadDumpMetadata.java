package com.perfma.thread.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Collections;

public class ThreadDumpMetadata {

    private String dumpTime;
    private String fileName;
    private int fileLine;
    private String fileContent;
    private int overallThreadsWaitingWithoutLocksCount;

    private Map<String, ThreadInfoModel> threads = new HashMap<String, ThreadInfoModel>();
    private Map<String, ThreadInfoModel> runnables = new HashMap<String, ThreadInfoModel>();
    private Map<String, ThreadInfoModel> blockings = new HashMap<String, ThreadInfoModel>();
    private Map<String, ThreadInfoModel> sleepings = new HashMap<String, ThreadInfoModel>();
    private Map<String, ThreadInfoModel> lockings = new HashMap<String, ThreadInfoModel>();

    private Map<String, MonitorInfoModel> monitors = new HashMap<String, MonitorInfoModel>();
    private Map<String, MonitorInfoModel> monitorsWithoutLocks = new HashMap<String, MonitorInfoModel>();
    private Map<String, MonitorInfoModel> monitorsWithLocks = new HashMap<String, MonitorInfoModel>();

    // block了一些线程的线程
    private Map<String, ThreadInfoModel> threadsBlockSome = new HashMap<String, ThreadInfoModel>();

    // 包含每条栈
    private Map<String, StackInfoModel> stackInfoMap = new HashMap<String, StackInfoModel>();

    private Map<String, StackInfoModel> sn2stackInfoModel = new HashMap<String, StackInfoModel>();

    private Map<HotStackType, List<StackInfoModel>> sortedStackInfoMap = new HashMap<HotStackType, List<StackInfoModel>>(); //
    // ArrayList<StackInfoModel>();
    private List<StackInfoModel> sortedStackInfoList = new ArrayList<StackInfoModel>();

    private String deadLockInfo;

    private int deadLockNum;

    private String analyzerReport;

    private String heapInfo;

    private ThreadInfoModel maxStackDepthThread;

    private int maxStackDepth;

    private ThreadInfoModel maxBlockNumThread;

    private int maxBlockNum;

    private MonitorInfoModel maxBlockNumMonitor;

    private List<String> threadIdList = new ArrayList<String>();

    public void addThreadBlockSome(ThreadInfoModel t) {
        threadsBlockSome.put(t.getTitle(), t);
    }

    public void addThread(ThreadInfoModel t) {
        threadIdList.add(t.getThreadId());
        threads.put(t.getTitle(), t);
    }

    public void addRunnableThread(ThreadInfoModel t) {
        runnables.put(t.getTitle(), t);
    }

    public void addBlockingThread(ThreadInfoModel t) {
        blockings.put(t.getTitle(), t);
    }

    public void addSleepingThread(ThreadInfoModel t) {
        sleepings.put(t.getTitle(), t);
    }

    public void addLockingThread(ThreadInfoModel t) {
        lockings.put(t.getTitle(), t);
    }

    public void addMonitorWithoutLocks(MonitorInfoModel m) {
        monitorsWithoutLocks.put(m.getLabel(), m);
        monitors.put(m.getLabel(), m);
    }

    public void addMonitorWithLocks(MonitorInfoModel m) {
        monitorsWithLocks.put(m.getLabel(), m);
        monitors.put(m.getLabel(), m);
    }

    // 主要用在解析过程中，不存在就创建，存在了就返回
    public StackInfoModel getStackInfo(String title, ThreadInfoModel thread, boolean runnable) {
        title = title.trim();
        StackInfoModel model = stackInfoMap.get(title);
        if (model == null) {
            model = new StackInfoModel(title);
            stackInfoMap.put(title, model);
            if (title.startsWith("at ")) {
                sn2stackInfoModel.put(model.getSn(), model);
            }
        }
        model.addThreadInfoModel(thread, runnable);
        return model;
    }

    // 通过sn找的要么存在，要么不存在，主要用在运行期各类查找
    public StackInfoModel getStackInfoBySn(String sn) {
        return sn2stackInfoModel.get(sn);
    }

    public void addStackInfo(StackInfoModel s) {
        stackInfoMap.put(s.getTitle(), s);
    }

    public MonitorInfoModel getMonitor(String label) {
        MonitorInfoModel model = monitors.get(label);
        if (model == null) {
            model = monitorsWithoutLocks.get(label);
        }
        return model;
    }

    public String getDumpTime() {
        return dumpTime;
    }

    public void setDumpTime(String dumpTime) {
        this.dumpTime = dumpTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileLine() {
        return fileLine;
    }

    public void setFileLine(int fileLine) {
        this.fileLine = fileLine;
    }

    public Map<String, ThreadInfoModel> getThreads() {
        return threads;
    }

    public void setThreads(Map<String, ThreadInfoModel> threads) {
        this.threads = threads;
    }

    public Map<String, ThreadInfoModel> getRunnables() {
        return runnables;
    }

    public void setRunnables(Map<String, ThreadInfoModel> runnables) {
        this.runnables = runnables;
    }

    public Map<String, ThreadInfoModel> getBlockings() {
        return blockings;
    }

    public void setBlockings(Map<String, ThreadInfoModel> blockings) {
        this.blockings = blockings;
    }

    public Map<String, ThreadInfoModel> getSleepings() {
        return sleepings;
    }

    public void setSleepings(Map<String, ThreadInfoModel> sleepings) {
        this.sleepings = sleepings;
    }

    public Map<String, ThreadInfoModel> getLockings() {
        return lockings;
    }

    public void setLockings(Map<String, ThreadInfoModel> lockings) {
        this.lockings = lockings;
    }

    public Map<String, MonitorInfoModel> getMonitors() {
        return monitors;
    }

    public void setMonitors(Map<String, MonitorInfoModel> monitors) {
        this.monitors = monitors;
    }

    public Map<String, MonitorInfoModel> getMonitorsWithoutLocks() {
        return monitorsWithoutLocks;
    }

    public void setMonitorsWithoutLocks(Map<String, MonitorInfoModel> monitorsWithoutLocks) {
        this.monitorsWithoutLocks = monitorsWithoutLocks;
    }

    public String getHeapInfo() {
        return heapInfo;
    }

    public void setHeapInfo(String heapInfo) {
        this.heapInfo = heapInfo;
    }

    public Map<String, ThreadInfoModel> getThreadsBlockSome() {
        return threadsBlockSome;
    }

    public void setThreadsBlockSome(Map<String, ThreadInfoModel> threadsBlockSome) {
        this.threadsBlockSome = threadsBlockSome;
    }

    public Map<String, MonitorInfoModel> getMonitorsWithLocks() {
        return monitorsWithLocks;
    }

    public void setMonitorsWithLocks(Map<String, MonitorInfoModel> monitorsWithLocks) {
        this.monitorsWithLocks = monitorsWithLocks;
    }

    public String getDeadLockInfo() {
        return deadLockInfo;
    }

    public void setDeadLockInfo(String deadLockInfo) {
        this.deadLockInfo = deadLockInfo;
    }

    public int getOverallThreadsWaitingWithoutLocksCount() {
        return overallThreadsWaitingWithoutLocksCount;
    }

    public void setOverallThreadsWaitingWithoutLocksCount(int overallThreadsWaitingWithoutLocksCount) {
        this.overallThreadsWaitingWithoutLocksCount = overallThreadsWaitingWithoutLocksCount;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getAnalyzerReport() {
        return analyzerReport;
    }

    public void setAnalyzerReport(String analyzerReport) {
        this.analyzerReport = analyzerReport;
    }

    public int getDeadLockNum() {
        return deadLockNum;
    }

    public void setDeadLockNum(int deadLockNum) {
        this.deadLockNum = deadLockNum;
    }

    public ThreadInfoModel getMaxStackDepthThread() {
        return maxStackDepthThread;
    }

    public void setMaxStackDepthThread(ThreadInfoModel maxStackDepthThread) {
        this.maxStackDepthThread = maxStackDepthThread;
    }

    public int getMaxStackDepth() {
        return maxStackDepth;
    }

    public void setMaxStackDepth(int maxStackDepth) {
        this.maxStackDepth = maxStackDepth;
    }

    public ThreadInfoModel getMaxBlockNumThread() {
        return maxBlockNumThread;
    }

    public void setMaxBlockNumThread(ThreadInfoModel maxBlockNumThread) {
        this.maxBlockNumThread = maxBlockNumThread;
    }

    public int getMaxBlockNum() {
        return maxBlockNum;
    }

    public void setMaxBlockNum(int maxBlockNum) {
        this.maxBlockNum = maxBlockNum;
    }

    public MonitorInfoModel getMaxBlockNumMonitor() {
        return maxBlockNumMonitor;
    }

    public void setMaxBlockNumMonitor(MonitorInfoModel maxBlockNumMonitor) {
        this.maxBlockNumMonitor = maxBlockNumMonitor;
    }

    public Map<String, StackInfoModel> getStackInfoMap() {
        return stackInfoMap;
    }

    public void setStackInfoMap(Map<String, StackInfoModel> stackInfoMap) {
        this.stackInfoMap = stackInfoMap;
    }

    public List<String> getThreadIdList() {
        return threadIdList;
    }

    public void setThreadIdList(List<String> threadIdList) {
        this.threadIdList = threadIdList;
    }

    public List<StackInfoModel> getSortedStackInfoList() {
        if (sortedStackInfoList == null || sortedStackInfoList.size() == 0) {
            sortStackInfoList();
        }
        return sortedStackInfoList;
    }

    public void sortStackInfoList() {
        sortedStackInfoList.addAll(sn2stackInfoModel.values());
        Comparator<StackInfoModel> comparator = new Comparator<StackInfoModel>() {
            @Override
            public int compare(StackInfoModel o1, StackInfoModel o2) {
                return o2.getThreadNum() - o1.getThreadNum();
            }
        };
        Collections.sort(sortedStackInfoList, comparator);
    }

    public List<StackInfoModel> getSortedStackInfoList(HotStackType stack_type) {
        List<StackInfoModel> sortedStackInfo = sortedStackInfoMap.get(stack_type);
        if (sortedStackInfo == null) {
            sortedStackInfo = sortStackInfoList(stack_type);
            sortedStackInfoMap.put(stack_type, sortedStackInfo);
        }
        return sortedStackInfo;
    }

    public List<StackInfoModel> sortStackInfoList(HotStackType stack_type) {
        List<StackInfoModel> sortedStackInfoList = new ArrayList<StackInfoModel>();
        sortedStackInfoList.addAll(sn2stackInfoModel.values());
        Comparator<StackInfoModel> comparator = null;
        if (stack_type == HotStackType.hot_stack_all_thread) {
            comparator = new Comparator<StackInfoModel>() {
                @Override
                public int compare(StackInfoModel o1, StackInfoModel o2) {
                    return o2.getThreadNum() - o1.getThreadNum();
                }
            };
        } else {
            comparator = new Comparator<StackInfoModel>() {
                @Override
                public int compare(StackInfoModel o1, StackInfoModel o2) {
                    return o2.getRunnableThreadNum() - o1.getRunnableThreadNum();
                }
            };
        }
        Collections.sort(sortedStackInfoList, comparator);
        return sortedStackInfoList;
    }

    public MonitorInfoModel getMonitorInfoModelByAddress(String address) {
        Iterator<MonitorInfoModel> it = this.monitors.values().iterator();
        while (it.hasNext()) {
            MonitorInfoModel model = it.next();
            if (model.getAddress().equals(address)) {
                return model;
            }
        }
        return null;
    }

    public ThreadInfoModel getThreadInfoModelByThreadId(String thread_id) {
        Iterator<ThreadInfoModel> it = this.threads.values().iterator();
        while (it.hasNext()) {
            ThreadInfoModel model = it.next();
            if (model.getThreadId().equals(thread_id)) {
                return model;
            }
        }
        return null;
    }

}
