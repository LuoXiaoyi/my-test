package com.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/9 22:20
 **/
public class Hello implements HelloMBean {
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printHello() {
        System.out.println("hello : " + name);
    }

    @Override
    public void printHello(String whoName) {
        System.out.println("hello : " + whoName);
    }

    @Override
        public ObjectName getObjectName() {
            ObjectName name = null;
            try {
                name = new ObjectName("MyMBean:name=HelloWorld");
            } catch (MalformedObjectNameException e) {
                e.printStackTrace();
            }
            return name;
    }
}
