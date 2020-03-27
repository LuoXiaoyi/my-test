package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * @author xiluo
 * @date 2020/3/25 18:24
 * @Version 1.0
 **/
public class Memory implements Serializable {
    private static final long serialVersionUID = 2312075028506474399L;
    /**
     * Total usable RAM (i.e. physical RAM minus a few reserved bits and the kernel binary code).
     */
    private long totalPhysicalMemorySize;

    /**
     * The sum of LowFree+HighFree.
     */
    private long freePhysicalMemorySize;

    /**
     * Available memory in system.
     */
    private long availablePhysicalMemorySize;

    /**
     * Relatively  temporary  storage for raw disk blocks that shouldn't get tremendously large (20MB or so).
     */
    private long bufferSize;

    /**
     * In-memory cache for files read from the disk (the page cache).  Doesn't include SwapCached
     * Memory used by the page cache and slabs (Cached and SReclaimable in /proc/meminfo)
     */
    private long cacheSize;

    /**
     * Total amount of swap space available.
     */
    private long totalSwapSpaceSize;

    /**
     * Amount of swap space that is currently unused.
     */
    private long freeSwapSpaceSize;

    /**
     * Memory which is waiting to get written back to the disk.
     */
    private long dirtySize;

    /**
     * since Linux 2.6.32, Memory used (mostly) by tmpfs, displayed as zero if not available
     */
    private long shareSize;

    public long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
    }

    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
    }

    public long getAvailablePhysicalMemorySize() {
        return availablePhysicalMemorySize;
    }

    public void setAvailablePhysicalMemorySize(long availablePhysicalMemorySize) {
        this.availablePhysicalMemorySize = availablePhysicalMemorySize;
    }

    public long getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(long bufferSize) {
        this.bufferSize = bufferSize;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getTotalSwapSpaceSize() {
        return totalSwapSpaceSize;
    }

    public void setTotalSwapSpaceSize(long totalSwapSpaceSize) {
        this.totalSwapSpaceSize = totalSwapSpaceSize;
    }

    public long getFreeSwapSpaceSize() {
        return freeSwapSpaceSize;
    }

    public void setFreeSwapSpaceSize(long freeSwapSpaceSize) {
        this.freeSwapSpaceSize = freeSwapSpaceSize;
    }

    public long getUsedSwapSpaceSize() {
        return this.totalSwapSpaceSize - this.freeSwapSpaceSize;
    }

    public long getDirtySize() {
        return dirtySize;
    }

    public void setDirtySize(long dirtySize) {
        this.dirtySize = dirtySize;
    }

    public long getShareSize() {
        return shareSize;
    }

    public void setShareSize(long shareSize) {
        this.shareSize = shareSize;
    }
}
