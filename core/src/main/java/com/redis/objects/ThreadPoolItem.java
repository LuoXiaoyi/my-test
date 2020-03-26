package com.redis.objects;

import java.io.Serializable;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/21 21:06
 **/
public class ThreadPoolItem implements Serializable {
    private static final long serialVersionUID = 8811469955264352648L;
    private long tid;
    private String threadName;

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "ThreadPoolItem{" +
                "tid=" + tid +
                ", threadName='" + threadName + '\'' +
                '}';
    }
}
