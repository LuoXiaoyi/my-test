package com.perfma.thread.model;

import java.util.ArrayList;
import java.util.List;

public class MonitorCatalogModel {
    private String                label;
    private List<ThreadInfoModel> threadInfos = new ArrayList<ThreadInfoModel>();

    public MonitorCatalogModel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ThreadInfoModel> getThreadInfos() {
        return threadInfos;
    }

    public void setThreadInfos(List<ThreadInfoModel> threadInfos) {
        this.threadInfos = threadInfos;
    }

    public void addThreadInfo(ThreadInfoModel model) {
        threadInfos.add(model);
    }
}
