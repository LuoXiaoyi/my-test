package com.healthcenter.model.os;

import java.util.Set;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/25 18:26
 * @Version 1.0
 **/
public class OsUser {
    private int id;
    private String name;
    private UserGroup mainGroup;

    /**
     * 若只属于一个组，则该对象为 null
     */
    private Set<UserGroup> groups;

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

    public UserGroup getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(UserGroup mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }
}
