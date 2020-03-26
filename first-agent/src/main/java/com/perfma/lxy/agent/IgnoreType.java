package com.perfma.lxy.agent;

/**
 * perfma-hotmethod-j9
 *
 * @author xiluo
 * @createTime 2020-01-02 17:36
 **/
public enum IgnoreType {
    // 无需被忽略
    NONE,
    IS_NULL,
    IS_PRIMITIVE,
    IS_ARRAY,
    IS_ENUM,
    IS_INTERFACE,
    IS_OBJECT,
    IS_UN_MODIFIABLE,
    IS_LAMBDA,
    // 系统类以及一些需要被忽略的包，如：asm、perfma、java 等包下的类
    IS_SYSTEM
}
