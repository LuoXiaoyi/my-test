package com.healthcenter.model.os;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/25 22:44
 * @Version 1.0
 **/
public class LimitEntity {
    private long softLimit;
    private long hardLimit;
    private LimitUnit unit;
    private String desc;

    public long getSoftLimit() {
        return softLimit;
    }

    public void setSoftLimit(long softLimit) {
        this.softLimit = softLimit;
    }

    public long getHardLimit() {
        return hardLimit;
    }

    public void setHardLimit(long hardLimit) {
        this.hardLimit = hardLimit;
    }

    public LimitUnit getUnit() {
        return unit;
    }

    public void setUnit(LimitUnit unit) {
        this.unit = unit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
