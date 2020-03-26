package com.perfma;

import com.sun.tools.attach.VirtualMachine;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-06-28 20:39
 **/
public class AgentAttacher {

    public static void main(String[] args) throws Exception {
/*

        if (args.length < 3) {
            System.out.println("3 args should be present[agentPath,paras,pid]");
            return;
        }
*/

        // System.out.println("pid: " + args[2] + ", agentPath: " + args[0] + ", paras: " + args[1]);
        String pid = "8152";//args[2];
       /* String encodeParas = encode(args[1]);*/
        String agentPath = "/Users/xiaoyiluo/Desktop/exceptional-agent-master/libagent.so" ;//args[0];

        VirtualMachine virtualMachine = VirtualMachine.attach(pid);
        virtualMachine.loadAgentPath(agentPath, "attach test");
        System.out.println("attach process: " + pid + " success.");
    }

    static String encode(String orgStr) {
        int[] pkey = {9, 1, 7, 8, 2};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orgStr.length(); i++) {
            sb.append(encodeChar(orgStr.charAt(i), pkey[i % 5]));
        }
        return sb.toString();
    }

    private static char encodeChar(char c, int key) {
        return (char) (c ^ key);
    }
}
