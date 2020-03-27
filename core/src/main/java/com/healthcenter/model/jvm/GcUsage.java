package com.healthcenter.model.jvm;

/**
 * @author xiluo
 * @date 2020/3/27 20:39
 * @Version 1.0
 **/
public class GcUsage {
    /**
     * sun.gc.generation.0.space.1.capacity
     */
    private long s0Capacity;

    /**
     * sun.gc.generation.0.space.2.capacity
     */
    private long s1Capacity;

    /**
     * sun.gc.generation.0.space.1.used
     */
    private long s0Used;

    /**
     * sun.gc.generation.0.space.2.used
     */
    private long s1Used;

    /**
     * sun.gc.generation.0.space.0.capacity
     */
    private long edenCapacity;

    /**
     * sun.gc.generation.0.space.0.used
     */
    private long edenUsed;

    /**
     * sun.gc.generation.1.space.0.capacity
     */
    private long oldCapacity;

    /**
     * sun.gc.generation.1.space.0.used
     */
    private long oldUsed;

    /**
     * sun.gc.metaspace.capacity
     */
    private long metaSpaceCapacity;

    /**
     * sun.gc.metaspace.used
     */
    private long metaSpaceUsed;

    /**
     * sun.gc.compressedclassspace.capacity
     */
    private long compressClassesSpaceCapacity;

    /**
     * sun.gc.compressedclassspace.used
     */
    private long compressClassesSpaceUsed;

    /**
     * sun.gc.collector.0.invocations
     */
    private long yGcCount;

    /**
     * sun.gc.collector.0.time/sun.os.hrt.frequency
     * second
     */
    private float yGcTime;

    /**
     * sun.gc.collector.1.invocations
     */
    private long fGcCount;

    /**
     * sun.gc.collector.1.time/sun.os.hrt.frequency
     * second
     */
    private float fGcTime;

    /**
     * (sun.gc.collector.0.time + sun.gc.collector.1.time)/sun.os.hrt.frequency
     * second
     */
    private float totalGcTime;

    /**
     *  sun.gc.lastCause
     */
    private String lastGcCause;

    /**
     * Current GC Cause
     */
    private String currentGcCause;

    public long getS0Capacity() {
        return s0Capacity;
    }

    public void setS0Capacity(long s0Capacity) {
        this.s0Capacity = s0Capacity;
    }

    public long getS1Capacity() {
        return s1Capacity;
    }

    public void setS1Capacity(long s1Capacity) {
        this.s1Capacity = s1Capacity;
    }

    public long getS0Used() {
        return s0Used;
    }

    public void setS0Used(long s0Used) {
        this.s0Used = s0Used;
    }

    public long getS1Used() {
        return s1Used;
    }

    public void setS1Used(long s1Used) {
        this.s1Used = s1Used;
    }

    public long getEdenCapacity() {
        return edenCapacity;
    }

    public void setEdenCapacity(long edenCapacity) {
        this.edenCapacity = edenCapacity;
    }

    public long getEdenUsed() {
        return edenUsed;
    }

    public void setEdenUsed(long edenUsed) {
        this.edenUsed = edenUsed;
    }

    public long getOldCapacity() {
        return oldCapacity;
    }

    public void setOldCapacity(long oldCapacity) {
        this.oldCapacity = oldCapacity;
    }

    public long getOldUsed() {
        return oldUsed;
    }

    public void setOldUsed(long oldUsed) {
        this.oldUsed = oldUsed;
    }

    public long getMetaSpaceCapacity() {
        return metaSpaceCapacity;
    }

    public void setMetaSpaceCapacity(long metaSpaceCapacity) {
        this.metaSpaceCapacity = metaSpaceCapacity;
    }

    public long getMetaSpaceUsed() {
        return metaSpaceUsed;
    }

    public void setMetaSpaceUsed(long metaSpaceUsed) {
        this.metaSpaceUsed = metaSpaceUsed;
    }

    public long getCompressClassesSpaceCapacity() {
        return compressClassesSpaceCapacity;
    }

    public void setCompressClassesSpaceCapacity(long compressClassesSpaceCapacity) {
        this.compressClassesSpaceCapacity = compressClassesSpaceCapacity;
    }

    public long getCompressClassesSpaceUsed() {
        return compressClassesSpaceUsed;
    }

    public void setCompressClassesSpaceUsed(long compressClassesSpaceUsed) {
        this.compressClassesSpaceUsed = compressClassesSpaceUsed;
    }

    public long getyGcCount() {
        return yGcCount;
    }

    public void setyGcCount(long yGcCount) {
        this.yGcCount = yGcCount;
    }

    public float getyGcTime() {
        return yGcTime;
    }

    public void setyGcTime(float yGcTime) {
        this.yGcTime = yGcTime;
    }

    public long getfGcCount() {
        return fGcCount;
    }

    public void setfGcCount(long fGcCount) {
        this.fGcCount = fGcCount;
    }

    public float getfGcTime() {
        return fGcTime;
    }

    public void setfGcTime(float fGcTime) {
        this.fGcTime = fGcTime;
    }

    public float getTotalGcTime() {
        return totalGcTime;
    }

    public void setTotalGcTime(float totalGcTime) {
        this.totalGcTime = totalGcTime;
    }

    public String getLastGcCause() {
        return lastGcCause;
    }

    public void setLastGcCause(String lastGcCause) {
        this.lastGcCause = lastGcCause;
    }

    public String getCurrentGcCause() {
        return currentGcCause;
    }

    public void setCurrentGcCause(String currentGcCause) {
        this.currentGcCause = currentGcCause;
    }
}
