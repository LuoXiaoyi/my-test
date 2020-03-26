package com.perfma.thread.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThreadInfoModel {

    private String                content;
    private String                info;
    private int                   stackLines;
    private boolean               aLotOfWaiting;
    private int                   childCount;

    private String                title;
    private String                name;
    private String                type;
    private String                prio;
    private String                threadId;
    private String                nativeId;
    private String                state;
    private String                addressRange;
    private String                descriptor;

    private int                   ownedMonitortNum;
    private int                   blockedThreadNum;
    //是否是伪造的一个ThreadInfoModel
    private boolean               dumy;

    private List<StackInfoModel>  children     = new ArrayList<StackInfoModel>();

    //该线程持有的monitor对象
    private Set<MonitorInfoModel> ownedMonitor = new HashSet<MonitorInfoModel>();

    public ThreadInfoModel() {
    }

    public ThreadInfoModel(String title) {
        this.setTitle(title);
    }

    public void addOwnedMonitor(MonitorInfoModel monitor) {
        ownedMonitor.add(monitor);
    }

    public void addChild(StackInfoModel t) {
        this.children.add(t);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isaLotOfWaiting() {
        return aLotOfWaiting;
    }

    public void setaLotOfWaiting(boolean aLotOfWaiting) {
        this.aLotOfWaiting = aLotOfWaiting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddressRange() {
        return addressRange;
    }

    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStackLines() {
        return stackLines;
    }

    public void setStackLines(int stackLines) {
        this.stackLines = stackLines;
    }

    public void setALotOfWaiting(boolean b) {
        aLotOfWaiting = b;
    }

    public boolean areALotOfWaiting() {
        return (aLotOfWaiting);
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public List<StackInfoModel> getChildren() {
        return children;
    }

    public void setChildren(List<StackInfoModel> children) {
        this.children = children;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public Set<MonitorInfoModel> getOwnedMonitor() {
        return ownedMonitor;
    }

    public void setOwnedMonitor(Set<MonitorInfoModel> ownedMonitor) {
        this.ownedMonitor = ownedMonitor;
    }

    public boolean isDumy() {
        return dumy;
    }

    public void setDumy(boolean dumy) {
        this.dumy = dumy;
    }

    public int getOwnedMonitortNum() {
        return ownedMonitortNum;
    }

    public void setOwnedMonitortNum(int ownedMonitortNum) {
        this.ownedMonitortNum = ownedMonitortNum;
    }

    public int getBlockedThreadNum() {
        return blockedThreadNum;
    }

    public void setBlockedThreadNum(int blockedThreadNum) {
        this.blockedThreadNum = blockedThreadNum;
    }

}
