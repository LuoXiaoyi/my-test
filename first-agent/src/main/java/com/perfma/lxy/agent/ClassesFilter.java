package com.perfma.lxy.agent;

/**
 * perfma-profiler-cpu-j9
 *
 * @author xiluo
 * @createTime 2019-12-18 13:33
 **/
public class ClassesFilter {

    /**
     * J9 中不支持的 retransform 的 class 包括如下类型
     * 1. 原生类型，如 int、boolean、short 等
     * 2. 数组类型，任意类型的数组，如：int[]、Object[]
     * 3. 枚举类型
     * 4. 接口
     * 5. java.lang.Object
     * 6. 被注解 com.ibm.oti.vm.J9UnmodifiableClass 注解的类
     *
     * @param cls 可能被 retransform 的类
     * @return 如果要被忽略，则返回 true，否则返回 false
     */
    public static IgnoreType ignoreTypeOf(Class cls) {
        if (cls == null) {
            return IgnoreType.IS_NULL;
        }

        if (cls.isPrimitive()) {
            return IgnoreType.IS_PRIMITIVE;
        }

        if (cls.isArray()) {
            return IgnoreType.IS_ARRAY;
        }

        if (cls.isEnum()) {
            return IgnoreType.IS_ENUM;
        }

        if (cls.isInterface()) {
            return IgnoreType.IS_INTERFACE;
        }

        // java.lang.Object should be ignored, j9 is not allowed.
        if (cls.getSuperclass() == null) {
            return IgnoreType.IS_OBJECT;
        }

        if (!AgentBootstrap.instrumentation.isModifiableClass(cls)) {
            return IgnoreType.IS_UN_MODIFIABLE;
        }

        Package name = cls.getPackage();
        if (name != null) {
            String pkgName = name.getName();
            // 过滤掉 sun 开头、ibm、org.objectweb.asm、java. 等系统相关的包，防止出现灵异事件
            for (String sysName : SYSTEM_PACKAGES) {
                if (pkgName.startsWith(sysName)) {
                    return IgnoreType.IS_SYSTEM;
                }
            }
        }

        // $$Lambda，这些类在 J9 中无法被成功增强，故提前去掉，提升性能
        if (cls.getName().contains("$$Lambda$")) {
            return IgnoreType.IS_LAMBDA;
        }


        return IgnoreType.NONE;
    }

    public static IgnoreType ignoreTypeOf(String internalClassName) {
        // 过滤掉 sun 开头、ibm、org.objectweb.asm、java. 等系统相关的包，防止出现灵异事件
        for (String sysName : SYSTEM_INTERNAL_PACKAGES) {
            if (internalClassName.startsWith(sysName)) {
                return IgnoreType.IS_SYSTEM;
            }
        }

        // $$Lambda，这些类在 J9 中无法被成功增强，故提前去掉，提升性能
        if (internalClassName.contains("$$Lambda$")) {
            return IgnoreType.IS_LAMBDA;
        }

        return IgnoreType.NONE;
    }

    /**
     * 被认为是系统的包，如果增强将可能造成相关的问题
     */
    private static final String[] SYSTEM_PACKAGES = {
            "sun.", "com.ibm", "org.objectweb.asm", "com.perfma",
            "java.", "javax.", "jdk.", "openj9.", "com.sun"};

    /**
     * 被认为是系统的包，如果增强将可能造成相关的问题，格式是以'/'分隔开
     */
    private static final String[] SYSTEM_INTERNAL_PACKAGES = {
            "sun/", "com/ibm", "org/objectweb/asm", "com/perfma",
            "java/", "javax/", "jdk/", "openj9/", "com/sun"};
}
