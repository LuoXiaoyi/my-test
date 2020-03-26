package com.test.perfma;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

import java.util.List;
import java.util.Scanner;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-12-10 14:32
 **/
public class JavaAgentTest {

    static final String TEST_HOME = "/home/xiluo/test/j9_hotmethod";

    public static void main(String[] args) throws Exception {
        listJavaPids();
        Scanner scanner = new Scanner(System.in);
        System.out.println("-> Please select pid: ");
        final String pid = scanner.next();

        System.out.println("attach to process: " + pid);
        VirtualMachine vm = VirtualMachine.attach(pid);

        if ("stop".equals(args[0])) {
            attachJavaAgent(vm, "STOP");
            attachJVMTIAgent(vm, "stop");
        } else {
            attachJavaAgent(vm, "START");
            attachJVMTIAgent(vm, "start");
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

    /**
     * check_xowl=true,
     * xowl_home_path=/home/admin/perfma/xowl,
     * agent_log_path=/home/admin/perfma/xowl/../logs/xowl,
     * event=cpu,command=start,
     * xowl_status_path=/home/admin/perfma/data/status/4363.running,
     * base_dir=/home/admin/perfma/data/module-data/hotmethod/1110/410733703635927040/,
     * file_pattern=mid2name.mn-cpu,flush=5,interval=10000000,
     * duration=0,max_stack_num=50,use_perf=false,diff_time=0,
     * socket_file_path=/home/admin/perfma/data/module-data/hotmethod/1110/410733703635927040/1110-cpu.s
     */
    static void attachJVMTIAgent(VirtualMachine vm, String cmd) throws Exception {
        final String jvmtiAgent = TEST_HOME + "/dlibs/libperfma-r2.1-linux-x64.so";
        StringBuilder cmdArgs = new StringBuilder();
        cmdArgs.append("check_xowl=false");
        cmdArgs.append(",xowl_home_path=").append(TEST_HOME);
        cmdArgs.append(",agent_log_path=").append(TEST_HOME).append("/logs/xowl");
        cmdArgs.append(",event=cpu");
        cmdArgs.append(",command=").append(cmd);
        cmdArgs.append(",xowl_status_path=").append(TEST_HOME).append("/.running");
        cmdArgs.append(",base_dir=").append(TEST_HOME).append("/data");
        cmdArgs.append(",file_pattern=mid2name.mn-cpu");
        cmdArgs.append(",flush=5,interval=10000000");
        cmdArgs.append(",duration=0,max_stack_num=50,use_perf=false,diff_time=0");
        cmdArgs.append(",socket_file_path=").append(TEST_HOME).append("cpu.s");

        System.out.format("begin to attach [jvmti agent:%s, args: %s]\n", jvmtiAgent, cmdArgs.toString());
        vm.loadAgentPath(jvmtiAgent, encode(cmdArgs.toString()));
    }

    /**
     * START;core.jar;LOG_LEVEL=DEBUG,xowl_home_path=xxxxx,
     */
    static void attachJavaAgent(VirtualMachine vm, String cmd) throws Exception {
        final String javaAgent = TEST_HOME + "/agents/perfma-hotmethod-j9-agent-1.0-SNAPSHOT-jar-with-dependencies.jar";
        final String javaAgentCore = TEST_HOME + "/agents/perfma-hotmethod-j9-core-1.0-SNAPSHOT-jar-with-dependencies.jar";
        StringBuilder cmdArgs = new StringBuilder();
        cmdArgs.append(cmd).append(";");
        cmdArgs.append(javaAgentCore).append(";");
        cmdArgs.append("LOG_LEVEL=DEBUG,xowl_home_path=").append(TEST_HOME);
        System.out.format("begin to attach [java agent:%s, args: %s]\n", javaAgent, cmdArgs.toString());
        vm.loadAgent(javaAgent, cmdArgs.toString());
    }

    static String encode(String args) {
        StringBuilder sb = new StringBuilder();
        int pkey[] = {9, 1, 7, 8, 2};
        for (int i = 0; i < args.length(); ++i) {
            sb.append((char) (args.charAt(i) ^ pkey[i % 5]));
        }

        return sb.toString();
    }
}
