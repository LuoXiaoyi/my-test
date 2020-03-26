package com.perfma.thread.model;

import java.util.ArrayList;
import java.util.List;

public class MonitorInfoModel {
    // 针对monitor对象是内存地址
    private String                address;
    // 该monitor对象所在的线程类
    private String                clazz;

    private String                label;
    private String                title;

    private MonitorCatalogModel   locked                = new MonitorCatalogModel("locked");
    private MonitorCatalogModel   waitOnMonitor         = new MonitorCatalogModel("wait on monitor");
    private MonitorCatalogModel   lockAndSleepOnMonitor = new MonitorCatalogModel(
                                                            "lock and sleep on monitor");
    private MonitorCatalogModel   lockAndWaitOnMonitor  = new MonitorCatalogModel(
                                                            "lock and wait on monitor");

    private List<ThreadInfoModel> blockedThread         = new ArrayList<ThreadInfoModel>();

    private int                   lockedThreadNum;
    private int                   waitOnThreadNum;
    private int                   lockAndSleepThreadNum;
    private int                   lockAndWaitOnThreadNum;

    private int                   blockedThreadNum;

    public MonitorInfoModel(String monitor) {
        address = monitor.substring(0, monitor.indexOf(">") + 1);
        clazz = monitor.substring(monitor.indexOf("(a ") + 3, monitor.length() - 1);
        label = monitor;
        title = "【" + address + "】【" + clazz + "】";
    }

    public boolean hasBlockedThread() {
        return blockedThread.size() > 0;
    }

    public void addBlockedThread(ThreadInfoModel t) {
        blockedThread.add(t);
        blockedThreadNum = blockedThread.size();
    }

    public void addLocked(ThreadInfoModel t) {
        locked.addThreadInfo(t);
        lockedThreadNum = locked.getThreadInfos().size();
    }

    public void addWaitOnMonitor(ThreadInfoModel t) {
        waitOnMonitor.addThreadInfo(t);
        waitOnThreadNum = waitOnMonitor.getThreadInfos().size();
    }

    public void addLockAndSleepOnMonitor(ThreadInfoModel t) {
        lockAndSleepOnMonitor.addThreadInfo(t);
        lockAndSleepThreadNum = lockAndSleepOnMonitor.getThreadInfos().size();
    }

    public void addLockAndWaitOnMonitor(ThreadInfoModel t) {
        lockAndWaitOnMonitor.addThreadInfo(t);
        lockAndWaitOnThreadNum = lockAndWaitOnMonitor.getThreadInfos().size();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public MonitorCatalogModel getLocked() {
        return locked;
    }

    public void setLocked(MonitorCatalogModel locked) {
        this.locked = locked;
    }

    public MonitorCatalogModel getWaitOnMonitor() {
        return waitOnMonitor;
    }

    public void setWaitOnMonitor(MonitorCatalogModel waitOnMonitor) {
        this.waitOnMonitor = waitOnMonitor;
    }

    public MonitorCatalogModel getLockAndSleepOnMonitor() {
        return lockAndSleepOnMonitor;
    }

    public void setLockAndSleepOnMonitor(MonitorCatalogModel lockAndSleepOnMonitor) {
        this.lockAndSleepOnMonitor = lockAndSleepOnMonitor;
    }

    public MonitorCatalogModel getLockAndWaitOnMonitor() {
        return lockAndWaitOnMonitor;
    }

    public void setLockAndWaitOnMonitor(MonitorCatalogModel lockAndWaitOnMonitor) {
        this.lockAndWaitOnMonitor = lockAndWaitOnMonitor;
    }

    public List<ThreadInfoModel> getBlockedThread() {
        return blockedThread;
    }

    public void setBlockedThread(List<ThreadInfoModel> blockedThread) {
        this.blockedThread = blockedThread;
    }

    public int getLockedThreadNum() {
        return lockedThreadNum;
    }

    public void setLockedThreadNum(int lockedThreadNum) {
        this.lockedThreadNum = lockedThreadNum;
    }

    public int getWaitOnThreadNum() {
        return waitOnThreadNum;
    }

    public void setWaitOnThreadNum(int waitOnThreadNum) {
        this.waitOnThreadNum = waitOnThreadNum;
    }

    public int getLockAndSleepThreadNum() {
        return lockAndSleepThreadNum;
    }

    public void setLockAndSleepThreadNum(int lockAndSleepThreadNum) {
        this.lockAndSleepThreadNum = lockAndSleepThreadNum;
    }

    public int getLockAndWaitOnThreadNum() {
        return lockAndWaitOnThreadNum;
    }

    public void setLockAndWaitOnThreadNum(int lockAndWaitOnThreadNum) {
        this.lockAndWaitOnThreadNum = lockAndWaitOnThreadNum;
    }

    public int getBlockedThreadNum() {
        return blockedThreadNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBlockedThreadNum(int blockedThreadNum) {
        this.blockedThreadNum = blockedThreadNum;
    }

}
