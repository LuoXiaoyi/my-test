package com.test.xiluo;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

import java.util.List;
import java.util.Scanner;

/**
 * test
 * check_xowl=true,xowl_home_path=/home/admin/perfma/xowl-test/xowl,
 * agent_log_path=/home/admin/perfma/xowl-test/xowl/../logs/xowl,
 * event=common,command=start,xowl_status_path=/home/admin/perfma/data/status/5748.running,
 * base_dir=/home/admin/perfma/data/module-data/memory/26586/434504324802936832/,
 * file_pattern=434504324802936832_26586.properties
 * @author xiluo
 * @createTime 2019-12-10 14:32
 **/
public class CommonAgentTest {
    static String TEST_HOME;
    static {
        TEST_HOME = System.getenv("TEST_HOME");
        if (TEST_HOME == null) {
            TEST_HOME = "/home/xiluo/test/agent_test/jvmti";
            System.out.println("Can not get TEST_HOME from env, use default: " + TEST_HOME);
        } else {
            System.out.println("Get TEST_HOME from env: " + TEST_HOME);
        }
    }

    public static void main(String[] args) throws Exception {
        listJavaPids();
        Scanner scanner = new Scanner(System.in);
        System.out.println("-> Please select pid: ");
        final String pid = scanner.next();
        System.out.println("attach to process: " + pid);
        VirtualMachine vm = VirtualMachine.attach(pid);
        final String cmd = args[0];
        final String jvmtiAgent = TEST_HOME + "/dlibs/libperfma-d2.5-linux-x64.so";
        System.out.format("begin to attach [jvmti agent:%s, args: %s]\n", jvmtiAgent, cmd);
        vm.loadAgentPath(jvmtiAgent, encode(cmd));
    }

    static void listJavaPids() {
        List<AttachProvider> pros = AttachProvider.providers();
        if (!pros.isEmpty()) {
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

    static String encode(String args) {
        StringBuilder sb = new StringBuilder();
        int pkey[] = {9, 1, 7, 8, 2};
        for (int i = 0; i < args.length(); ++i) {
            sb.append((char) (args.charAt(i) ^ pkey[i % 5]));
        }
        return sb.toString();
    }
}
