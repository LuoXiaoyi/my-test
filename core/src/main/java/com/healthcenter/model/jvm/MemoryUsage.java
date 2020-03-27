package com.healthcenter.model.jvm;

import java.io.Serializable;

/**
 * @author xiluo
 * @date 2020/3/27 10:50
 * @Version 1.0
 **/
public class MemoryUsage implements Serializable {
    private static final long serialVersionUID = -8076472613882888091L;
    private long init;
    private long used;
    private long committed;
    private long max;

    public static MemoryUsage from(java.lang.management.MemoryUsage mu) {
        MemoryUsage myMu = new MemoryUsage();
        myMu.init = mu.getInit();
        myMu.used = mu.getUsed();
        myMu.committed = mu.getCommitted();
        myMu.max = mu.getMax();
        return myMu;
    }

    public long getInit() {
        return init;
    }

    public void setInit(long init) {
        this.init = init;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getCommitted() {
        return committed;
    }

    public void setCommitted(long committed) {
        this.committed = committed;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}
