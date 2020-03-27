package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * @author xiluo
 * @description 参考 df 的实现
 * @date 2020/3/25 18:24
 * @Version 1.0
 **/
public class Disk implements Serializable {
    private static final long serialVersionUID = -6428477780196461198L;
    private String fileSystem;
    private long size;
    private long used;
    private long avail;
    private double usePercent;
    private String mountedOn;

    public String getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getAvail() {
        return avail;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }

    public double getUsePercent() {
        return usePercent;
    }

    public void setUsePercent(double usePercent) {
        this.usePercent = usePercent;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public void setMountedOn(String mountedOn) {
        this.mountedOn = mountedOn;
    }
}
