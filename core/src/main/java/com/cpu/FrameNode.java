package com.cpu;

import java.util.LinkedList;
import java.util.List;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-26 14:25
 **/
public class FrameNode {

    public FrameNode(long id, long samples) {
        this.id = id;
        this.samples = samples;
    }

    public FrameNode() {
    }

    /**
     * 方法 id
     */
    long id;

    /**
     * 采样次数
     */
    long samples;

    /**
     * 从该栈开始存在调用分叉，则调用分叉的栈将会被截断，并且将截断后的栈保留在此集合中，如果没有分叉，则该集合为空
     */
    List<StackNode> divideStacks;

    public boolean hasDivideStacks() {
        return divideStacks != null && !divideStacks.isEmpty();
    }

    public void addDivideStack(StackNode sn) {
        if (divideStacks == null) {
            divideStacks = new LinkedList<>();
        }

        divideStacks.add(sn);
    }

    public void addDivideStacks(List<StackNode> sns) {
        if (divideStacks == null) {
            divideStacks = new LinkedList<>();
        }

        divideStacks.addAll(sns);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSamples() {
        return samples;
    }

    public void setSamples(long samples) {
        this.samples = samples;
    }

    public List<StackNode> getDivideStacks() {
        return divideStacks;
    }

    public void setDivideStacks(List<StackNode> divideStacks) {
        this.divideStacks = divideStacks;
    }
}
