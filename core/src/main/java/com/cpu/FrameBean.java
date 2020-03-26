package com.cpu;

import java.io.Serializable;

/**
 * @author xiluo
 * @createTime 2019-05-26 15:35
 **/
public class FrameBean implements Serializable {
    private static final long serialVersionUID = -4144354803092349476L;

    /**
     * 任务的 id 和 时间戳
     */
    private String taskId;
    private long timeStamp;

    /**
     * 所属的栈 id
     */
    private int stackId;

    /**
     * 方法的基本信息，唯一标识，用于查找方法名称
     */
    private long methodId;

    /**
     * 方法在栈中的位置，若为 0，则表示是栈的第一帧
     */
    private short stackSeq;

    /**
     * 该方法被调用采样到的次数，用于计算百分比
     */
    private long samples;

    /**
     * 表示在图形中展现时，若为 true，则该节点会作为根节点展现，否则不是
     */
    private boolean root;

    /**
     * 只有在分叉栈帧上才有，即如下
     * A -- B
     * | -- C
     * <p>
     * A 就会有slotId，而 B、C 才会有 parentSlotId
     */
    private String slotId;
    private String parentSlotId;

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

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

    public int getStackId() {
        return stackId;
    }

    public void setStackId(int stackId) {
        this.stackId = stackId;
    }

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
        this.methodId = methodId;
    }

    public short getStackSeq() {
        return stackSeq;
    }

    public void setStackSeq(short stackSeq) {
        this.stackSeq = stackSeq;
    }

    public long getSamples() {
        return samples;
    }

    public void setSamples(long samples) {
        this.samples = samples;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getParentSlotId() {
        return parentSlotId;
    }

    public void setParentSlotId(String parentSlotId) {
        this.parentSlotId = parentSlotId;
    }

    @Override
    public String toString() {
        return "FrameBean{" +
                "taskId='" + taskId + '\'' +
                ", timeStamp=" + timeStamp +
                ", stackId='" + stackId + '\'' +
                ", methodId=" + methodId +
                ", stackSeq=" + stackSeq +
                ", samples=" + samples +
                ", root=" + root +
                ", slotId='" + slotId + '\'' +
                ", parentSlotId='" + parentSlotId + '\'' +
                '}';
    }
}
