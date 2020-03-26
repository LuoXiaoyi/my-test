package com.test.perfma;

import com.perfma.toolkit.common.util.FileSocketServer;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-12-10 14:32
 **/
public class JVMTIAgentTest {

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
        final String cmd = args.length >= 1 ? args[0] : "start";
        final String checkXowl = args.length >= 2 ? args[1] : null;
        final String fileSocket = args.length >= 3 ? args[2] : null;
        attachJVMTIAgent(vm, cmd, checkXowl, fileSocket);
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
    static void attachJVMTIAgent(VirtualMachine vm, String cmd, String checkOwl, String fileSocket) throws Exception {
        final String jvmtiAgent = TEST_HOME + "/dlibs/libperfma-d2.5-linux-x64.so";
        StringBuilder cmdArgs = new StringBuilder();
        final String xowlStatusPath = TEST_HOME + "/.running";
        System.out.println("xowlStatusPath: " + xowlStatusPath);
        if ("true".equals(checkOwl)) {
            cmdArgs.append("check_xowl=true");
            // 启动一个线程，模拟 xowl 更新状态文件的情况，运行两分钟后，线程会停止，从而模拟 xowl 进程挂掉的情景
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final long start = System.currentTimeMillis();
                    while (true) {
                        File f = new File(xowlStatusPath);
                        try {
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // 没隔 10s 跟新一次状态文件
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if (System.currentTimeMillis() - start >= 2 * 60 * 1000) {
                            break;
                        }
                    }

                    System.out.println("xowl status file update thread will exit.");
                }
            }, "check_xowl_thread").start();
        } else if ("false".equals(checkOwl)) {
            cmdArgs.append("check_xowl=false");
        }

        final String cpuSocket = TEST_HOME + "/cpu.s";
        if ("true".equals(fileSocket)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSocketServer fileSocketServer = new FileSocketServer();
                    try {
                        fileSocketServer.bind(cpuSocket);
                        fileSocketServer.accept();
                        System.out.println("client connect success.");
                    } catch (Exception ignore) {
                    } finally {
                        try {
                            fileSocketServer.close();
                            System.out.println("fileSocket server closed.");
                        } catch (IOException ignore) {
                        }
                    }
                }
            }).start();
            // 当前线程简单睡 3s，确保 新建线程启动好，即 filesocket 的 server 启动好
            Thread.sleep(3000);
        }

        cmdArgs.append(",xowl_home_path=").append(TEST_HOME);
        cmdArgs.append(",agent_log_path=").append(TEST_HOME).append("/logs/xowl");
        cmdArgs.append(",event=cpu");
        cmdArgs.append(",command=").append(cmd);
        cmdArgs.append(",xowl_status_path=").append(xowlStatusPath);
        cmdArgs.append(",base_dir=").append(TEST_HOME).append("/data");
        cmdArgs.append(",file_pattern=mid2name.mn-cpu");
        cmdArgs.append(",flush=5,interval=10000000");
        cmdArgs.append(",duration=0,max_stack_num=50,use_perf=false,diff_time=0");
        cmdArgs.append(",socket_file_path=").append(cpuSocket);

        System.out.format("begin to attach [jvmti agent:%s, args: %s]\n", jvmtiAgent, cmdArgs.toString());
        vm.loadAgentPath(jvmtiAgent, encode(cmdArgs.toString()));
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
