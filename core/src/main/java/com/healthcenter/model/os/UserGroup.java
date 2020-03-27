package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * @author xiluo
 * @date 2020/3/25 22:42
 * @Version 1.0
 **/
public class UserGroup implements Serializable {
    private static final long serialVersionUID = -7623168253955336730L;
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroup group = (UserGroup) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
