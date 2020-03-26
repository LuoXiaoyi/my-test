package com.es.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/29 10:06
 **/
public class HotMethodFrameBean implements Serializable {

    private static final long serialVersionUID = 2381794106346153555L;
    private String taskId;

    private long timeStamp;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * 方法名称（包括类全路径）
     */
    private String methodName;

    /**
     * 采样次数
     */
    private long sampleCnt;

    /**
     * 所在栈中位置的唯一标识
     */
    private String slotId;

    /**
     * 上一级栈帧的唯一标识
     */
    private String slotParentId;

    /**
     * 是否有下一级栈帧
     */
    private boolean hasChildren;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getSampleCnt() {
        return sampleCnt;
    }

    public void setSampleCnt(long sampleCnt) {
        this.sampleCnt = sampleCnt;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSlotParentId() {
        return slotParentId;
    }

    public void setSlotParentId(String slotParentId) {
        this.slotParentId = slotParentId;
    }

    public boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Override
    public String toString() {
        return "HotMethodFrameBean{" +
                "taskId='" + taskId + '\'' +
                ", timeStamp=" + timeStamp +
                ", methodName='" + methodName + '\'' +
                ", sampleCnt=" + sampleCnt +
                ", slotId='" + slotId + '\'' +
                ", slotParentId='" + slotParentId + '\'' +
                ", hasChildren=" + hasChildren +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotMethodFrameBean that = (HotMethodFrameBean) o;
        return timeStamp == that.timeStamp &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskId, timeStamp);
    }
}
