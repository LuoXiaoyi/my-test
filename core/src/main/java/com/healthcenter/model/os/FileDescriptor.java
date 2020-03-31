package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/31 21:29
 * @Version 1.0
 **/
public class FileDescriptor implements Serializable {
    private static final long serialVersionUID = -1962253668441133640L;
    private int fd;
    private FDType fdType;

    public static enum FDType {
        // 普通文件
        FILE,
        // socket 文件
        SOCKET,
        // 管道文件
        PIPE,
        // 匿名文件
        ANON
    }

    public int getFd() {
        return fd;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }

    public FDType getFdType() {
        return fdType;
    }

    public void setFdType(FDType fdType) {
        this.fdType = fdType;
    }
}
