package com.jmx.jconsole;

import com.sun.tools.attach.*;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.HotSpotVirtualMachine;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Properties;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/13 15:14
 **/
public class OneJVM {

    public static void main(String[] args) {

        String targetVmId = "33547";
        try {
            // 列出所有的 AttachProvider，Oracle JDK 下默认的实现是 BsdAttachProvider，且只会有这一个
            List<AttachProvider> providers = AttachProvider.providers();
            // 获取所有的虚拟机实例，这里获取的虚拟机实例均为可以 attachable 的，即不包含 disable 掉 attachable 能力的 VM id
            // HotSpotVirtualMachineDescriptor 一般都是这种描述符
            List<VirtualMachineDescriptor> vms = VirtualMachine.list();
            System.out.println("vms: " + vms.size());

            long start = System.currentTimeMillis();
            VirtualMachine vm = VirtualMachine.attach(targetVmId);

            // 做线程 dump
            InputStream threadDumpFileStream = ((HotSpotVirtualMachine) vm).remoteDataDump(args);
            if (threadDumpFileStream != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(threadDumpFileStream));

                System.out.println("Thread dumps: ");

                String line = reader.readLine();

                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();
                }

            }
            System.out.println("Thread dump cost total " + (System.currentTimeMillis() - start) + " ms");

            Properties sysProps = vm.getSystemProperties();
            System.out.println("getSystemProperties: " + sysProps);
            System.out.println("===================================================");
            String javaHome = sysProps.getProperty("java.home");
            System.out.println("java.home: " + javaHome);
            String jmxAgent = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
            System.out.println("agentPath: " + jmxAgent);
            vm.loadAgent(jmxAgent, "com.sun.management.jmxremote");

            Properties properties = vm.getAgentProperties();
            System.out.println("getAgentProperties: " + properties);
            String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
            System.out.println("address: " + address);

            vm.detach();

            JMXServiceURL url = new JMXServiceURL(address);
            JMXConnector connector = JMXConnectorFactory.connect(url);
            RuntimeMXBean rmxb = ManagementFactory.newPlatformMXBeanProxy(connector
                    .getMBeanServerConnection(), ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);

            System.out.println("getBootClassPath: " + rmxb.getBootClassPath());

            ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    connector.getMBeanServerConnection(), ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class
            );

            ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
            System.out.println("threadInfos.size() : " + threadInfos.length);
            System.out.println("\n\n\n Thread infos: \n");
            for (ThreadInfo thread : threadInfos) {
                System.out.println(thread + "\n");
            }
            boolean flag = threadMXBean.isThreadContentionMonitoringEnabled();
            threadMXBean.findDeadlockedThreads();
            threadMXBean.findMonitorDeadlockedThreads();
            System.out.println("flag: " + flag);
        } catch (AttachNotSupportedException e) {
            System.err.println("Attach to target VM [" + targetVmId + "] error because of \""
                    + e.getMessage() + "\", please check whether the target VM disabled the attach mechanism by" +
                    " using -XX:+DisableAttachMechanism");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Attach to target VM [" + targetVmId + "] error because of \""
                    + e.getMessage() + "\", please check whether the target VM disabled the attach mechanism by" +
                    " using -XX:+DisableAttachMechanism");
            e.printStackTrace();
        } catch (AgentLoadException e) {
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            e.printStackTrace();
        }

    }
}
