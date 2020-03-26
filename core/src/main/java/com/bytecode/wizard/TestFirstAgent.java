package com.bytecode.wizard;

import com.sun.tools.attach.VirtualMachine;
import sun.misc.Signal;

import java.util.Scanner;

/**
 * @author xiluo
 * @ClassName
 * @Description TODO
 * @Date 2020/1/18 00:33
 * @Version 1.0
 **/
public class TestFirstAgent {
    public static void main(String[] args) throws Exception {
        WizardTest.listJavaPids();
        Scanner scanner = new Scanner(System.in);
        System.out.println("-> Please select pid: ");
        final String pid = scanner.next();
        VirtualMachine vm = VirtualMachine.attach(pid);

        String agentJarPath = "/Users/xiaoyiluo/Documents/my-space/code/test/first-agent/target/first-agent-jar-with-dependencies.jar";
        vm.loadAgent(agentJarPath, "true");
        System.out.println("-> Attach to pid: " + pid + " OK");
    }


}
