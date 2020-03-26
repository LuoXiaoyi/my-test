package com;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-22 17:21
 **/
public class MqMessage {
    /**
     * 任务 id
     */
    private Long taskId;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 文件类型
     */
    String fileType;

    /**
     * 文件的长度
     */
    long fileLength;

    /**
     * 文件在 fastdfs 的key
     */
    private String fileKey;

    /**
     * 业务系统
     */
    private String biz;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }
}

