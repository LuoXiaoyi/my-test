package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * @author xiluo
 * @date 2020/3/25 18:24
 * @Version 1.0
 **/
public class Os implements Serializable {
    private static final long serialVersionUID = -2016373909325178363L;
    private String kernelVersion;
    private OsType osType;
    private OsArch osArch;

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public OsType getOsType() {
        return osType;
    }

    public void setOsType(OsType osType) {
        this.osType = osType;
    }

    public OsArch getOsArch() {
        return osArch;
    }

    public void setOsArch(OsArch osArch) {
        this.osArch = osArch;
    }
}
