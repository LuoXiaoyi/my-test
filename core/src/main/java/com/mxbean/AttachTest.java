package com.mxbean;

import com.sun.tools.attach.VirtualMachine;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.util.Properties;

/**
 * /Library/Java/JavaVirtualMachines/jdk1.8.0_151.jdk/Contents/Home/lib/tools.jar
 * @author xiluo
 * @createTime 2019/3/18 15:50
 **/
public class AttachTest {

    public static void main(String[] args) {
        String pid = "5954";
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
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
