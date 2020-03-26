package com;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author xiluo
 * @createTime 2019/4/11 10:10
 **/
public class AttachClient {

    public static void main(String[] args)
            throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {

        VirtualMachine vm = VirtualMachine.attach(args[1]);
        System.out.println("attach pid " + args[1] + " OK...");
        vm.loadAgentPath(args[2], args[3]);
        System.out.println("load agent [" + args[2] + "] with paras[" + args[3] + "] OK...");
    }
}
