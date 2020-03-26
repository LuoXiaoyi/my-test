package com.jmx;

import com.sun.tools.attach.VirtualMachine;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/22 17:40
 **/
public class Attach {

    public static void main(String[] args) throws Exception {
        VirtualMachine vm = VirtualMachine.attach(args[0]);
        if (args.length >= 2) {
            vm.loadAgentPath(args[1], args[2]);
            System.out.format("laod agent[%s] to pid[%s], args[%s].", args[1],args[0],args[2]);
        } else {
            vm.loadAgentPath(args[1], "test");
            System.out.format("laod agent[%s] to pid[%s], args[%s].", args[1],args[0],"test");
        }
        System.out.println();
    }
}
