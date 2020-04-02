package com.mxbean;

import com.sun.tools.attach.VirtualMachine;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Properties;

import static java.lang.management.ManagementFactory.*;

/**
 * /Library/Java/JavaVirtualMachines/jdk1.8.0_151.jdk/Contents/Home/lib/tools.jar
 *
 * @author xiluo
 * @createTime 2019/3/18 15:50
 **/
public class AttachTest {

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            String pid = args[0];
            System.out.println("pid: " + pid);
            try {
                VirtualMachine virtualMachine = VirtualMachine.attach(pid);

                Properties sysProps = virtualMachine.getSystemProperties();
                String javaHome = sysProps.getProperty("java.home");
                String jmxAgent = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
                virtualMachine.loadAgent(jmxAgent, "com.sun.management.jmxremote");
                System.out.println("Success in loading JMX agent, path is: " + jmxAgent);

                Properties properties = virtualMachine.getAgentProperties();
                String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
                System.out.println("com.sun.management.jmxremote.localConnectorAddress is: {}" + address);
                System.out.println("agent properties: " + properties);

                // detach.
                virtualMachine.detach();

                JMXServiceURL jmxServiceURL = new JMXServiceURL(address);
                JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL);
                MBeanServerConnection mBeanServerConnection = connector.getMBeanServerConnection();

                ObjectName threadMxBeanName = new ObjectName(THREAD_MXBEAN_NAME);
                boolean hasPlatformMXBeans = mBeanServerConnection.isRegistered(threadMxBeanName);
                if (hasPlatformMXBeans) {
                    RuntimeMXBean runtimeMBean =
                            newPlatformMXBeanProxy(mBeanServerConnection, RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);

                    System.out.println("+++++++++++++ getInputArguments: " + runtimeMBean.getInputArguments());
                    System.out.println("+++++++++++++ getSystemProperties: " + runtimeMBean.getSystemProperties());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        FileSystem fs = FileSystems.getDefault();
        Iterable<FileStore> fileStores = fs.getFileStores();
        for (FileStore fileStore : fileStores) {
            System.out.println("summary: " + fileStore.toString());
            System.out.println("fs name: " + fileStore.name());
            System.out.println("fs type: " + fileStore.type());
            System.out.println("fs total space: " + fileStore.getTotalSpace());
            System.out.println("fs unused space: " + fileStore.getUsableSpace());
            System.out.println("fs unallocated space: " + fileStore.getUnallocatedSpace());


            Class<?> ufsClz = Class.forName("sun.nio.fs.UnixFileStore");
            if (ufsClz.isAssignableFrom(fileStore.getClass())) {
                Method entry = ufsClz.getDeclaredMethod("entry");
                entry.setAccessible(true);
                Object eObj = entry.invoke(fileStore);
                Method dir = eObj.getClass().getDeclaredMethod("dir");
                dir.setAccessible(true);
                byte[] dirBytes = (byte[]) dir.invoke(eObj);
                if(dirBytes != null){
                    System.out.println("mounted on: " + new String(dirBytes));
                }
            }

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }
}
