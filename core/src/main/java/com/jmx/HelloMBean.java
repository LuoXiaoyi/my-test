package com.jmx;

import java.lang.management.PlatformManagedObject;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/9 22:19
 **/
public interface HelloMBean extends PlatformManagedObject {

    public String getName();
    public void setName(String name);
    public void printHello();
    public void printHello(String whoName);
}
