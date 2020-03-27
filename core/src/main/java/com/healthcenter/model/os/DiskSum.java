package com.healthcenter.model.os;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiluo
 * @date 2020/3/26 18:54
 * @Version 1.0
 **/
public class DiskSum  implements Serializable {
    private static final long serialVersionUID = -262466834180548815L;
    private double tps;
    private double readBytesPerSec;
    private double writeBytesPerSec;
    private double totalReadBytes;
    private double totalWriteBytes;

    /**
     * 花在 disk IO 上的时间
     */
    private double totalIoMs;

    /**
     * 处于 IO 状态的请求数量
     */
    private long currentlyInProgress;
    private List<Disk> disks;

    public double getTotalIoMs() {
        return totalIoMs;
    }

    public void setTotalIoMs(double totalIoMs) {
        this.totalIoMs = totalIoMs;
    }

    public double getTps() {
        return tps;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public double getReadBytesPerSec() {
        return readBytesPerSec;
    }

    public void setReadBytesPerSec(double readBytesPerSec) {
        this.readBytesPerSec = readBytesPerSec;
    }

    public double getWriteBytesPerSec() {
        return writeBytesPerSec;
    }

    public void setWriteBytesPerSec(double writeBytesPerSec) {
        this.writeBytesPerSec = writeBytesPerSec;
    }

    public double getTotalReadBytes() {
        return totalReadBytes;
    }

    public void setTotalReadBytes(double totalReadBytes) {
        this.totalReadBytes = totalReadBytes;
    }

    public double getTotalWriteBytes() {
        return totalWriteBytes;
    }

    public void setTotalWriteBytes(double totalWriteBytes) {
        this.totalWriteBytes = totalWriteBytes;
    }

    public long getCurrentlyInProgress() {
        return currentlyInProgress;
    }

    public void setCurrentlyInProgress(long currentlyInProgress) {
        this.currentlyInProgress = currentlyInProgress;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }
}
