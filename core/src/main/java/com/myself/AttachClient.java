package com.myself;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

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

    private AttachClient(String targetVmId) {
        this.targetVmId = targetVmId;
    }

    /**
     * @param targetVmId 目标进程 ID
     */
    public static AttachClient buildClient(String targetVmId) {
        AttachClient attachClient = new AttachClient(targetVmId);
        attachClient.init();
        return attachClient;
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

    public <T> T getMxBean(Class<T> clazz, ObjectName objectName) throws IOException {
        /*Set<ObjectInstance> set = mBeanServerConnection.queryMBeans(null, null);
        for(Iterator<ObjectInstance> it = set.iterator();it.hasNext();){
            ObjectInstance oi = it.next();
            System.out.println("\t"+oi.getObjectName() + ", class: " + oi.getClassName());
        }*/

        T proxy = MBeanServerInvocationHandler.newProxyInstance(
                mBeanServerConnection, objectName, clazz, false);

        return proxy;
    }

    public Collection<MemoryPoolMXBean> getMemoryPoolMXBeans() throws IOException {
        return getMXBeans(MemoryPoolMXBean.class, MEMORY_POOL_MXBEAN_DOMAIN_TYPE);
    }

    public Collection<GarbageCollectorMXBean> getGarbageCollectorMXBeans() throws IOException {
        return getMXBeans(GarbageCollectorMXBean.class, GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE);
    }

    private void init() {
        try {
            virtualMachine = VirtualMachine.attach(targetVmId);

            Properties sysProps = virtualMachine.getSystemProperties();
            String javaHome = sysProps.getProperty("java.home");
            String jmxAgent = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
            virtualMachine.loadAgent(jmxAgent, "com.sun.management.jmxremote");
            System.out.println("Success in loading JMX agent, path is: {}" + jmxAgent);

            Properties properties = virtualMachine.getAgentProperties();
            String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
            System.out.println("com.sun.management.jmxremote.localConnectorAddress is: {}" + address);

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

            System.out.println("Success in attaching process[ {} ]." + targetVmId);
        } catch (AttachNotSupportedException e) {
            String msg = "Attach to target VM [" + targetVmId + "] error because of \""
                    + e.getMessage() + "\", please check whether the target VM disabled the attach mechanism by" +
                    " using -XX:+DisableAttachMechanism";
            System.out.println(msg);
            throw new InternalError(e);
        } catch (IOException | AgentLoadException |
                AgentInitializationException | MalformedObjectNameException |
                IntrospectionException |
                InstanceNotFoundException |
                ReflectionException e) {
            System.err.println(e.getMessage());
            throw new InternalError(e);
        }
    }

    private <T> Collection<T> getMXBeans(Class<T> mxBeansClass, String mxBeansClassName) throws IOException {

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

    // private Logger logger = LoggerFactory.getLogger(this.getClass());
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
