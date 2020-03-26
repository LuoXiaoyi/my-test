package com.redis.objects;

import java.io.Serializable;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/18 15:22
 **/
public class ThreadInfo implements Serializable {
    private static final long serialVersionUID = -6112047088294994994L;

    public String name;
    public long tid;
    public Long lwpId;
    public long timeStamp;
    public String taskId;

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "name='" + name + '\'' +
                ", tid=" + tid +
                ", lwpId=" + lwpId +
                ", timeStamp=" + timeStamp +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
