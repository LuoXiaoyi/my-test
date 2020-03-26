package com.healthcenter.model.os;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/25 18:26
 * @Version 1.0
 **/
public class ProcessLimit {
    private LimitEntity maxCpuTimeLimit;
    private LimitEntity maxFileSize;
    private LimitEntity maxDataSize;
    private LimitEntity maxStackSize;
    private LimitEntity maxCoreFileSize;
    private LimitEntity maxProcesses;
    private LimitEntity maxOpenFiles;
    private LimitEntity maxLockedMemory;
    private LimitEntity maxAddressSpace;
    private LimitEntity maxFileLocks;
    private LimitEntity maxPendingSignals;
    private LimitEntity maxMsgQueueSize;
    private LimitEntity maxNicePriority;
    private LimitEntity maxRealTimePriority;
    private LimitEntity maxRealTimeTimeout;

    public LimitEntity getMaxCpuTimeLimit() {
        return maxCpuTimeLimit;
    }

    public void setMaxCpuTimeLimit(LimitEntity maxCpuTimeLimit) {
        this.maxCpuTimeLimit = maxCpuTimeLimit;
    }

    public LimitEntity getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(LimitEntity maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public LimitEntity getMaxDataSize() {
        return maxDataSize;
    }

    public void setMaxDataSize(LimitEntity maxDataSize) {
        this.maxDataSize = maxDataSize;
    }

    public LimitEntity getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(LimitEntity maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public LimitEntity getMaxCoreFileSize() {
        return maxCoreFileSize;
    }

    public void setMaxCoreFileSize(LimitEntity maxCoreFileSize) {
        this.maxCoreFileSize = maxCoreFileSize;
    }

    public LimitEntity getMaxProcesses() {
        return maxProcesses;
    }

    public void setMaxProcesses(LimitEntity maxProcesses) {
        this.maxProcesses = maxProcesses;
    }

    public LimitEntity getMaxOpenFiles() {
        return maxOpenFiles;
    }

    public void setMaxOpenFiles(LimitEntity maxOpenFiles) {
        this.maxOpenFiles = maxOpenFiles;
    }

    public LimitEntity getMaxLockedMemory() {
        return maxLockedMemory;
    }

    public void setMaxLockedMemory(LimitEntity maxLockedMemory) {
        this.maxLockedMemory = maxLockedMemory;
    }

    public LimitEntity getMaxAddressSpace() {
        return maxAddressSpace;
    }

    public void setMaxAddressSpace(LimitEntity maxAddressSpace) {
        this.maxAddressSpace = maxAddressSpace;
    }

    public LimitEntity getMaxFileLocks() {
        return maxFileLocks;
    }

    public void setMaxFileLocks(LimitEntity maxFileLocks) {
        this.maxFileLocks = maxFileLocks;
    }

    public LimitEntity getMaxPendingSignals() {
        return maxPendingSignals;
    }

    public void setMaxPendingSignals(LimitEntity maxPendingSignals) {
        this.maxPendingSignals = maxPendingSignals;
    }

    public LimitEntity getMaxMsgQueueSize() {
        return maxMsgQueueSize;
    }

    public void setMaxMsgQueueSize(LimitEntity maxMsgQueueSize) {
        this.maxMsgQueueSize = maxMsgQueueSize;
    }

    public LimitEntity getMaxNicePriority() {
        return maxNicePriority;
    }

    public void setMaxNicePriority(LimitEntity maxNicePriority) {
        this.maxNicePriority = maxNicePriority;
    }

    public LimitEntity getMaxRealTimePriority() {
        return maxRealTimePriority;
    }

    public void setMaxRealTimePriority(LimitEntity maxRealTimePriority) {
        this.maxRealTimePriority = maxRealTimePriority;
    }

    public LimitEntity getMaxRealTimeTimeout() {
        return maxRealTimeTimeout;
    }

    public void setMaxRealTimeTimeout(LimitEntity maxRealTimeTimeout) {
        this.maxRealTimeTimeout = maxRealTimeTimeout;
    }
}
