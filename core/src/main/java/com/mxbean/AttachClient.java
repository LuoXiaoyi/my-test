package com.mxbean;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.lang.management.*;
import java.util.*;

import static java.lang.management.ManagementFactory.*;

/**
 * 通过 Attach 的方式，获取目标 JVM 进程（仅适合本地进程，通过 RMI 方式）的 MXBean 的信息
 * 该方式可以用于实时数据抽取和分析
 * 注意：该方式如果要使用，需要注意以下两点：
 * 1. 运行时需要将 tools.jar 包放置在 Classpath 下
 * 2. 被 attach 的 JVM 进程必须是开启了 DisableAttachMechanism 选项的（默认开启），如果禁用，无法正常使用
 * 一般是在 JVM 启动参数中加入了 -XX:+DisableAttachMechanism 这个参数来关闭掉 attach 机制
 *
 * @author xiaoyiluo
 * @createTime 2018/6/14 14:41
 **/
public class AttachClient {

    public static void main(String[] args) throws IOException {
        AttachClient ac = AttachClient.buildClient("870");
        ThreadMXBean tbx = ac.getThreadMXBean();
        ThreadInfo[] tis = tbx.dumpAllThreads(false, false);

        long a[] = new long[]{Long.MAX_VALUE};

        ThreadInfo[] tis2 = tbx.getThreadInfo(a);
        System.out.println("done.");
    }

    /**
     * 根据 ObjectName 获取 MBeanInfo 的信息
     *
     * @param name MBean name
     * @return MBeanInfo，对应的 MBean 的实例，如：sun.management.MemoryPoolImpl
     * @throws InstanceNotFoundException ""
     * @throws IntrospectionException    ""
     * @throws ReflectionException       ""
     * @throws IOException               ""
     */
    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException, IOException {
        return this.mBeanServerConnection.getMBeanInfo(name);
    }

    /**
     * 根据 ObjectName 获取 ObjectName 的信息，该 ObjectName 可以是采用*号通配符的，如：
     * “java.lang:type=GarbageCollector,*”，这样就会把“java.lang:type=GarbageCollector”
     * 的所有 ObjectName 获取到
     * 如果二者（name 和 query）都为空，则返回的是 PlatformMxbeanServer 上所有的 Mbean names
     *
     * @param name  MBean name
     * @param query 查询条件
     * @return 所有匹配的 ObjectName
     * @throws IOException ""
     */
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query) throws IOException {
        return this.mBeanServerConnection.queryNames(name, query);
    }

    /**
     * @param targetVmId 目标进程 ID
     */
    public static AttachClient buildClient(String targetVmId) {
        AttachClient attachClient = new AttachClient(targetVmId);
        attachClient.init();
        return attachClient;
    }

    public boolean isConnectionOk() {
        boolean isOk = false;
        if (this.mBeanServerConnection != null) {
            try {
                this.mBeanServerConnection.getMBeanCount();
                isOk = true;
            } catch (IOException e) {
                logger.error("connection error.", e);
            }
        }
        return isOk;
    }

    public ClassLoadingMXBean getClassLoadingMXBean() throws IOException {
        if (hasPlatformMXBeans && classLoadingMXBean == null) {
            synchronized (this) {
                if (hasPlatformMXBeans && classLoadingMXBean == null) {
                    classLoadingMXBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, CLASS_LOADING_MXBEAN_NAME,
                                    ClassLoadingMXBean.class);
                }
            }
        }

        return classLoadingMXBean;
    }

    public CompilationMXBean getCompilationMXBean() throws IOException {
        if (hasCompilationMXBean && compilationMXBean == null) {
            synchronized (this) {
                if (hasCompilationMXBean && compilationMXBean == null) {
                    compilationMXBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, COMPILATION_MXBEAN_NAME,
                                    CompilationMXBean.class);
                }
            }
        }

        return compilationMXBean;
    }

    public MemoryMXBean getMemoryMXBean() throws IOException {
        if (hasPlatformMXBeans && memoryMBean == null) {
            synchronized (this) {
                if (hasPlatformMXBeans && memoryMBean == null) {
                    memoryMBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, MEMORY_MXBEAN_NAME,
                                    MemoryMXBean.class);
                }
            }
        }

        return memoryMBean;
    }

    public RuntimeMXBean getRuntimeMXBean() throws IOException {
        if (hasPlatformMXBeans && runtimeMBean == null) {
            synchronized (this) {
                if (hasPlatformMXBeans && runtimeMBean == null) {
                    runtimeMBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, RUNTIME_MXBEAN_NAME,
                                    RuntimeMXBean.class);
                }
            }
        }

        return runtimeMBean;
    }

    public ThreadMXBean getThreadMXBean() throws IOException {
        if (hasPlatformMXBeans && threadMBean == null) {
            synchronized (this) {
                if (hasPlatformMXBeans && threadMBean == null) {
                    threadMBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, THREAD_MXBEAN_NAME,
                                    ThreadMXBean.class);
                }
            }
        }

        return threadMBean;
    }

    public OperatingSystemMXBean getOperatingSystemMXBean() throws IOException {
        if (hasPlatformMXBeans && operatingSystemMBean == null) {
            synchronized (this) {
                if (hasPlatformMXBeans && operatingSystemMBean == null) {
                    operatingSystemMBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, OPERATING_SYSTEM_MXBEAN_NAME,
                                    OperatingSystemMXBean.class);
                }
            }
        }
        return operatingSystemMBean;
    }

    public com.sun.management.OperatingSystemMXBean getSunOperatingSystemMXBean() throws IOException {
        if (sunOperatingSystemMXBean == null) {
            synchronized (this) {
                try {
                    if (sunOperatingSystemMXBean == null) {
                        ObjectName on = new ObjectName(OPERATING_SYSTEM_MXBEAN_NAME);
                        if (mBeanServerConnection.isInstanceOf(on,
                                "com.sun.management.OperatingSystemMXBean")) {
                            sunOperatingSystemMXBean =
                                    newPlatformMXBeanProxy(mBeanServerConnection,
                                            OPERATING_SYSTEM_MXBEAN_NAME,
                                            com.sun.management.OperatingSystemMXBean.class);
                        }
                    }
                } catch (InstanceNotFoundException e) {
                    return null;
                } catch (MalformedObjectNameException e) {
                    return null; // should never reach here
                }
            }
        }
        return sunOperatingSystemMXBean;
    }

    public HotSpotDiagnosticMXBean getHotSpotDiagnosticMXBean() throws IOException {
        if (hasHotSpotDiagnosticMXBean && hotspotDiagnosticMXBean == null) {
            synchronized (this) {
                if (hasHotSpotDiagnosticMXBean && hotspotDiagnosticMXBean == null) {
                    hotspotDiagnosticMXBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, HOTSPOT_DIAGNOSTIC_MXBEAN_NAME,
                                    HotSpotDiagnosticMXBean.class);
                }
            }
        }
        return hotspotDiagnosticMXBean;
    }

    public Collection<MemoryPoolMXBean> getMemoryPoolMXBeans() throws IOException {
        return getPlatformMxBeans(MemoryPoolMXBean.class, MEMORY_POOL_MXBEAN_DOMAIN_TYPE);
    }

    public Collection<GarbageCollectorMXBean> getGarbageCollectorMXBeans() throws IOException {
        return getPlatformMxBeans(GarbageCollectorMXBean.class, GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE);
    }

    public <T> T getMxBean(Class<T> clazz, String objectName) {
        if (clazz == null || objectName == null)
            return null;

        ObjectName object = null;
        try {
            object = new ObjectName(objectName);
        } catch (MalformedObjectNameException e) {
            logger.error("new ObjectName error: ", e);
            return null;
        }

        return getMxBean(clazz, object);
    }

    public <T> T getMxBean(Class<T> clazz, ObjectName objectName) {
        if (clazz == null || objectName == null)
            return null;

        return MBeanServerInvocationHandler.newProxyInstance(
                mBeanServerConnection, objectName, clazz, false);
    }

    public <T> Collection<T> getPlatformMxBeans(Class<T> mxBeansClass, String mxBeansClassName) throws IOException {

        Set<T> collectorMXBeans = null;
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(mxBeansClassName + ",*");
        } catch (MalformedObjectNameException e) {
            // should not reach here
            assert (false);
        }

        if (objectName != null) {
            Set<ObjectName> mbeans = mBeanServerConnection.queryNames(objectName, null);
            if (mbeans != null && !mbeans.isEmpty()) {
                collectorMXBeans = new HashSet<>();
                Iterator<ObjectName> iterator = mbeans.iterator();
                while (iterator.hasNext()) {
                    ObjectName on = iterator.next();
                    String ObName = mxBeansClassName +
                            ",name=" + on.getKeyProperty("name");
                    T mBean = newPlatformMXBeanProxy(mBeanServerConnection, ObName,
                            mxBeansClass);
                    collectorMXBeans.add(mBean);
                }
            }
        }

        return collectorMXBeans;
    }

    private void init() {
        try {
            virtualMachine = VirtualMachine.attach(targetVmId);

            Properties sysProps = virtualMachine.getSystemProperties();
            String javaHome = sysProps.getProperty("java.home");
            String jmxAgent = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
            virtualMachine.loadAgent(jmxAgent, "com.sun.management.jmxremote");
            logger.trace("Success in loading JMX agent, path is: {}" + jmxAgent);

            Properties properties = virtualMachine.getAgentProperties();
            String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
            logger.trace("com.sun.management.jmxremote.localConnectorAddress is: {}" + address);
            System.out.println("agent properties: " + properties);

            // detach.
            virtualMachine.detach();

            JMXServiceURL jmxServiceURL = new JMXServiceURL(address);
            JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL);
            mBeanServerConnection = connector.getMBeanServerConnection();

            ObjectName threadMxBeanName = new ObjectName(THREAD_MXBEAN_NAME);
            this.hasPlatformMXBeans = mBeanServerConnection.isRegistered(threadMxBeanName);

            this.hasHotSpotDiagnosticMXBean =
                    mBeanServerConnection.isRegistered(new ObjectName(HOTSPOT_DIAGNOSTIC_MXBEAN_NAME));
            // check if it has 6.0 new APIs
            if (this.hasPlatformMXBeans) {
                MBeanOperationInfo[] mopis = mBeanServerConnection.getMBeanInfo(threadMxBeanName).getOperations();
                // look for findDeadlockedThreads operations;
                for (MBeanOperationInfo op : mopis) {
                    if (op.getName().equals("findDeadlockedThreads")) {
                        this.supportsLockUsage = true;
                        break;
                    }
                }

                ObjectName compilationMXBeanName = new ObjectName(COMPILATION_MXBEAN_NAME);
                this.hasCompilationMXBean = mBeanServerConnection.isRegistered(compilationMXBeanName);
            }

            logger.info("Success in attaching process[ {} ].", targetVmId);
        } catch (AttachNotSupportedException e) {
            String msg = "Attach to target VM [" + targetVmId + "] error because of \""
                    + e.getMessage() + "\", please check whether the target VM disabled the attach mechanism by" +
                    " using -XX:+DisableAttachMechanism";
            logger.error(msg, e);
            throw new InternalError(e);
        } catch (IOException | AgentLoadException |
                AgentInitializationException | MalformedObjectNameException |
                IntrospectionException |
                InstanceNotFoundException |
                ReflectionException e) {
            logger.error(e.getMessage());
            throw new InternalError(e);
        }
    }

    public String getTargetVmId() {
        return targetVmId;
    }

    private AttachClient(String targetVmId) {
        this.targetVmId = targetVmId;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String targetVmId;
    private VirtualMachine virtualMachine;
    private MBeanServerConnection mBeanServerConnection;
    private boolean hasPlatformMXBeans;
    private boolean hasHotSpotDiagnosticMXBean;
    private boolean supportsLockUsage;
    private boolean hasCompilationMXBean;
    private ClassLoadingMXBean classLoadingMXBean;
    private CompilationMXBean compilationMXBean;
    private com.sun.management.OperatingSystemMXBean sunOperatingSystemMXBean;
    private HotSpotDiagnosticMXBean hotspotDiagnosticMXBean;
    private OperatingSystemMXBean operatingSystemMBean;
    private ThreadMXBean threadMBean;
    private RuntimeMXBean runtimeMBean;
    private MemoryMXBean memoryMBean;
    private final static String HOTSPOT_DIAGNOSTIC_MXBEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

}
