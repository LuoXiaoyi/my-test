package com.cpu;

import java.util.LinkedList;
import java.util.List;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-26 15:28
 **/
public class StackNode {

    public StackNode(int stackId) {
        this.stackId = stackId;
    }

    public StackNode() {
        this.stackId = 0;
    }

    private String taskId;
    private long timeStamp;
    private int stackId;
    private int mesh;

    private List<FrameNode> frames;
    /**
     * 只有与当前栈从栈顶开始，前 n（n>0） 个栈帧是相同的才能被加入其中，否则将会合并失败
     */
    private List<StackNode> toBeMergedStacks;
    private long samples;

    /**
     * 获取该栈中所有的栈帧，如果有从其他栈合并过来的栈帧，也包含在里面，并且帧与帧之间的关系也已经描述好了
     */
    public List<FrameBean> queryAllStackFramesAsRootStackNode() {
        return queryAllStackFrames(0, null);
    }

    public int getMesh() {
        return mesh;
    }

    public void setMesh(int mesh) {
        this.mesh = mesh;
    }

    public void setStackId(int stackId) {
        this.stackId = stackId;
    }

    /**
     * 如果该栈存在需要合并的栈则合并，否则什么也不做
     */
    public void merge() {
        if (size() > 0 && shouldBeMerged()) {
            for (StackNode sn : toBeMergedStacks) {
                doMergeOneByOne(sn);
            }

            // 将所有的分叉口中的栈再次进行合并，如果需要的话
            for (FrameNode fn : frames) {
                fn.divideStacks = MergeUtil.mergeStackNodes(fn.getDivideStacks());
            }

            // merge 完毕之后置空，避免重复 merge
            toBeMergedStacks = null;
        }
    }

    /**
     * 获取当前栈从第 idx 个栈帧开始的所有栈的子栈
     *
     * @param idx 开始的栈的顺序，顺序从 0 开始
     * @return 子栈，如果 idx 为 0，则返回的是自己
     */
    public StackNode subStackNode(int idx) {
        if (idx < 0 || idx > size()) {
            throw new IllegalArgumentException("idx should be the value in [0," + size() + "]");
        }

        if (idx == 0) {
            return this;
        } else {
            StackNode subSN = new StackNode(this.stackId);
            subSN.frames = frames.subList(idx, size());
            subSN.samples = samples;
            return subSN;
        }
    }

    /**
     * 添加栈帧
     *
     * @param fn 被添加的栈帧
     */
    public void add(FrameNode fn) {
        if (frames == null) {
            frames = new LinkedList<>();
        }

        frames.add(fn);
    }

    /**
     * 获取第 idx 位置的栈帧，idx 为[0~size()-1]
     */
    public FrameNode get(int idx) {
        return idx < size() ? frames.get(idx) : null;
    }

    public boolean shouldBeMerged() {
        return toBeMergedStacks != null && !toBeMergedStacks.isEmpty();
    }

    public void addToBeMergedStack(StackNode sn) {
        if (toBeMergedStacks == null) {
            toBeMergedStacks = new LinkedList<>();
        }

        toBeMergedStacks.add(sn);
    }

    public int size() {
        return frames == null ? 0 : frames.size();
    }

    public List<FrameNode> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameNode> frames) {
        this.frames = frames;
    }

    public long getSamples() {
        return samples;
    }

    public void setSamples(long samples) {
        this.samples = samples;
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

    /**
     * 获取该栈的所有栈帧
     *
     * @param stackSeq 该栈下的所有栈帧都以 stackSeq 为基准计算栈帧在栈中的位置
     * @param pSlotId  该栈下所有的栈帧的 pSlotId 均为这个值
     * @return 该栈中所有的栈帧 bean 对象
     */
    private List<FrameBean> queryAllStackFrames(int stackSeq, String pSlotId) {
        if (size() == 0) {
            return null;
        }

        String tmpPSlotId = pSlotId;
        List<FrameBean> retBeans = new LinkedList<>();
        for (int i = 0; i < size(); ++i) {
            FrameNode fn = frames.get(i);
            FrameBean fb = new FrameBean();
            if (i == 0) {
                fb.setRoot(true);
            }
            fb.setTaskId(taskId);
            fb.setTimeStamp(timeStamp);
            fb.setMethodId(fn.id);
            fb.setSamples(fn.samples);
            fb.setStackSeq((short) (i + stackSeq));
            fb.setParentSlotId(tmpPSlotId);
            fb.setStackId(stackId);

            if (fn.hasDivideStacks()) {
                String slotId = IdGen.uuid();
                retBeans.add(fb);
                for (int j = 0; j < fn.getDivideStacks().size(); ++j) {
                    List<FrameBean> divideBeans = fn.getDivideStacks().get(j).queryAllStackFrames(i + 1, slotId);
                    if (divideBeans != null) {
                        retBeans.addAll(divideBeans);
                        fb.setSlotId(slotId);
                        // 当前栈的后续帧都放到 fn 的下一级显示出来
                        tmpPSlotId = slotId;
                        fb.setRoot(true);
                    }
                }
            } else {
                retBeans.add(fb);
            }
        }

        return retBeans;
    }

    /**
     * 将 otherStack 与当前栈对象进行合并，合并的栈必须从栈顶开始，至少存在一个栈帧与当前栈的相同位置的栈帧相同，否则会抛出异常
     *
     * @param otherStack 被合并的栈
     */
    private void doMergeOneByOne(StackNode otherStack) {
        if (otherStack.size() > 0) {
            int size = Math.min(frames.size(), otherStack.size());
            for (int i = 0; i < size; ++i) {
                if (frames.get(i).id == otherStack.get(i).id) {
                    frames.get(i).samples += otherStack.get(i).samples;
                    if (i == 0) {
                        // 只计算一次，因为 stack 的
                        samples += otherStack.samples;
                    }

                    // 将相关分支也进行合并
                    if (otherStack.get(i).hasDivideStacks()) {
                        frames.get(i).addDivideStacks(otherStack.get(i).getDivideStacks());
                    }
                } else {
                    if (i == 0) {
                        // 两个栈从栈顶开始，前 n(n>0) 个栈必须相等才能合并
                        throw new IllegalArgumentException("One or more stack frames from the stack top " +
                                "should be same in these two stacks.");
                    }
                    // 不相等，则将此元素开始后的所有元素都添加到当前栈的前一个节点的分叉栈中
                    frames.get(i - 1).addDivideStack(otherStack.subStackNode(i));
                    break;
                }
            }
        }
    }
}
