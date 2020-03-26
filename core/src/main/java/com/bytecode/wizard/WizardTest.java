package com.bytecode.wizard;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-09 17:34
 **/
public class WizardTest {

    public static void main(String[] args) throws Exception {

        listJavaPids();
        Scanner scanner = new Scanner(System.in);
        System.out.println("-> Please select pid: ");
        final String pid = scanner.next();
        if (pid != null && pid.length() != 0 && (pid.charAt(0) > '0' && pid.charAt(0) < '9')) {
            System.out.println("-> Please select java(j) or jvmti(v) agent: ");
            String type = scanner.next();
            System.out.println("-> Your selection is : " + type);

            if ("j".equals(type)) {
                attachJavaAgent(pid);
            } else {
                attachJVMTIAgent(pid);
            }
        }

        System.out.println("-> Please select cmd [t/r/e] to trace or reset or exit.");
        String cmd = scanner.next();
        if ("t".equals(cmd)) {
            traceTest();
        } else if ("r".equals(cmd)) {
            retset();
        } else {
            System.exit(0);
        }
    }

    static void listJavaPids() {
        List<AttachProvider> pros = AttachProvider.providers();
        if (pros != null && !pros.isEmpty()) {
            AttachProvider provider = pros.get(0);
            List<VirtualMachineDescriptor> machines = provider.listVirtualMachines();
            for (VirtualMachineDescriptor descriptor : machines) {
                if (descriptor.displayName().length() > 50) {
                    System.out.println("pid:" + descriptor.id() + " -> " + descriptor.displayName().substring(0, 50));
                } else {
                    System.out.println("pid:" + descriptor.id() + " -> " + descriptor.displayName());
                }
            }
        }
    }

    static void retset() throws Exception {
        // reset
        final String cmd = "6:323456\n";
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(20000));
        socket.getOutputStream().write(cmd.getBytes());
        System.out.println("reset success...");
    }

    static void traceTest() throws Exception {
        // trace
        final String cmd = "1:323456:10:100:0:0:1:java.io.FileInputStream:read0" + "\n";
        //final String cmd = "1:323456:10:100:0:0:1:com.bytecode.newi.ObjectHelper:getObjectSize" + "\n";
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(20000));
        socket.getOutputStream().write(cmd.getBytes());
        System.out.println("send trace success...");
    }

    static void attachJavaAgent(String pid) throws Exception {
        System.out.println("begin to attachJavaAgent pid: " + pid);
        final String userHome = System.getProperty("user.dir");
        final String wizardDir = "/Users/xiaoyiluo/Documents/PerfMa/code/perfma-wizard/";

        String agentJarPath = wizardDir + "perfma-wizard-agent-2.2.0-RELEASE.jar";

        StringBuilder agentArgs = new StringBuilder(wizardDir + "perfma-wizard-core-2.2.0-RELEASE.jar;");
        agentArgs.append("check_xowl=false,");
        agentArgs.append("xowl_home_path=" + userHome + ",");
        agentArgs.append("agent_log_path=" + userHome + "/logs,");
        agentArgs.append("event=wizard,command=start,");
        agentArgs.append("xowl_status_path=" + userHome + "/status/50.running,");
        agentArgs.append("xowl_status_path=" + userHome + "/status/50.running,");
        agentArgs.append("base_dir=" + userHome + "/monkey,");
        agentArgs.append("file_pattern=wizard-common-%s.log,traceRealObject=true,");
        agentArgs.append("server_ip=127.0.0.1,log_level=debug,server_port=20000,pid=" + pid + ",");

        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent(agentJarPath, agentArgs.toString());
        System.out.println("-> Attach to pid: " + pid + " OK");
    }

    static void attachJVMTIAgent(String pid) throws Exception {
        System.out.println("begin to attachJVMTIAgent pid: " + pid);
        final String agentPath = "/Users/xiaoyiluo/Documents/my-space/code/object-size/libnew.dylib";
        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgentPath(agentPath);
        System.out.println("-> Attach to pid: " + pid + " OK");
    }
}
