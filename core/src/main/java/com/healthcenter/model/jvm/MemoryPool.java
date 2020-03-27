package com.healthcenter.model.jvm;

import java.io.Serializable;

/**
 * @author xiluo
 * @date 2020/3/27 10:48
 * @Version 1.0
 **/
public class MemoryPool implements Serializable {
    private static final long serialVersionUID = -6161695459401133855L;
    /**
     * Heap or Non-Heap
     */
    private String type;

    /**
     * Based on different GC will have different value
     * PS Eden Space/PS Survivor Space/PS Old Gen
     */
    private String name;
    private MemoryUsage currentUsage;
    private MemoryUsage collectionUsage;
    private MemoryUsage peakUsage;

    /**
     * the usage threshold value of this memory pool in bytes.
     */
    private long usageThreshold;

    /**
     * the number of times that the memory usage has crossed its usage threshold value.
     */
    private long usageThresholdCount;

    /**
     * the collection usage threshold of this memory pool in bytes.
     */
    private long collectionUsageThreshold;

    /**
     * the number of times that the memory usage has reached or exceeded the collection usage threshold.
     */
    private long collectionUsageThresholdCount;

    /**
     * whether the memory usage of this memory pool
     * reaches or exceeds the collection usage threshold value
     * in the most recent collection;
     */
    private boolean isCollectionUsageThresholdExceeded;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MemoryUsage getCurrentUsage() {
        return currentUsage;
    }

    public void setCurrentUsage(MemoryUsage currentUsage) {
        this.currentUsage = currentUsage;
    }

    public MemoryUsage getCollectionUsage() {
        return collectionUsage;
    }

    public void setCollectionUsage(MemoryUsage collectionUsage) {
        this.collectionUsage = collectionUsage;
    }

    public MemoryUsage getPeakUsage() {
        return peakUsage;
    }

    public void setPeakUsage(MemoryUsage peakUsage) {
        this.peakUsage = peakUsage;
    }

    public long getUsageThreshold() {
        return usageThreshold;
    }

    public void setUsageThreshold(long usageThreshold) {
        this.usageThreshold = usageThreshold;
    }

    public long getUsageThresholdCount() {
        return usageThresholdCount;
    }

    public void setUsageThresholdCount(long usageThresholdCount) {
        this.usageThresholdCount = usageThresholdCount;
    }

    public long getCollectionUsageThreshold() {
        return collectionUsageThreshold;
    }

    public void setCollectionUsageThreshold(long collectionUsageThreshold) {
        this.collectionUsageThreshold = collectionUsageThreshold;
    }

    public long getCollectionUsageThresholdCount() {
        return collectionUsageThresholdCount;
    }

    public void setCollectionUsageThresholdCount(long collectionUsageThresholdCount) {
        this.collectionUsageThresholdCount = collectionUsageThresholdCount;
    }

    public boolean isCollectionUsageThresholdExceeded() {
        return isCollectionUsageThresholdExceeded;
    }

    public void setCollectionUsageThresholdExceeded(boolean collectionUsageThresholdExceeded) {
        isCollectionUsageThresholdExceeded = collectionUsageThresholdExceeded;
    }
}
