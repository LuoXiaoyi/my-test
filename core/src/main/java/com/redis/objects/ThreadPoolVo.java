package com.redis.objects;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/21 21:02
 **/
public class ThreadPoolVo implements Serializable, Comparable<ThreadPoolVo> {

    private String name;
    private int accCnt;
    private int crtCnt;

    private List<ThreadPoolItem> add;
    private List<ThreadPoolItem> del;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccCnt() {
        return accCnt;
    }

    public void setAccCnt(int accCnt) {
        this.accCnt = accCnt;
    }

    public int getCrtCnt() {
        return crtCnt;
    }

    public void setCrtCnt(int crtCnt) {
        this.crtCnt = crtCnt;
    }

    public List<ThreadPoolItem> getAdd() {
        return add;
    }

    public void setAdd(List<ThreadPoolItem> add) {
        this.add = add;
    }

    public List<ThreadPoolItem> getDel() {
        return del;
    }

    public void setDel(List<ThreadPoolItem> del) {
        this.del = del;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = -510123323992165808L;

    @Override
    public String toString() {
        return "ThreadPoolVo{" +
                "name='" + name + '\'' +
                ", accCnt=" + accCnt +
                ", crtCnt=" + crtCnt +
                ", add=" + add +
                ", del=" + del +
                '}';
    }

    @Override
    public int compareTo(ThreadPoolVo o) {
        if (o == null || o.name == null || name == null) return 0;
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreadPoolVo poolVo = (ThreadPoolVo) o;
        return Objects.equals(name, poolVo.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
