package com.proc.aix;

import java.io.*;
import java.util.Arrays;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-03 17:09
 **/
public class CmdLine {

    public static void main(String[] args) throws IOException {
        String pid = args[0];
        System.out.println("pid: " + pid);
        // readFromPsInfo();
        readFormPsCmd(pid);
    }

    static String readFormPsCmd(String pid) throws IOException {
        String cmd[] = {"sh", "-c", "ps -elf|grep " + pid + " |grep -v grep"};
        Process process = Runtime.getRuntime().exec(cmd);

        if (process != null) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String args = parse(line, pid);
                if (args != null) {
                    return args;
                }
            }
        }

        return null;
    }

    static void testArgs() {
        // aix
        String line = "  240001 A     root 2949592       1   0  60 20 8d05ed590 15548        * 15:04:32  pts/1  0:06 /home/perfma/license-server/jre/bin/java -Xms256m -Xmx256m -Xmn128m -agentpath:/home/perfma/license-server/dlibs/libperfma_doraemon_server-r2.1-aix-ppc-x64.so -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -cp /home/perfma/license-server/lib/* -Djava.security.egd=file:/dev/./urandom -Dperfma.config.dir=/home/perfma/license-server/config -Dlog.root=/home/perfma/license-server/../logs/licence_server com.perfma.licenseserver.Server";

        // linux
        String line2 = "4 S root     12974     0 99  80   0 - 2804164 futex_ Sep02 pts/2  1-13:21:47 /root/data/jdk1.8.0_151/bin/java -Xmx2688M -Xms2688M -Xmn960M -XX:-Inline -XX:-TieredCompilation -XX:ReservedCodeCacheSize=256M -XX:InitialCodeCacheSize=256M -cp /root/data/simulator/lib/* -Djava.security.egd=file:/dev/./urandom -Dperfma.config.dir=/root/data/simulator/config -Dapp.path=/root/data/simulator -Dlog.root=/root/data/simulator/logs demo.simulators.boot.ThreadSimulatorMain15";
        parse(line, "2949592");
        parse(line2, "12974");
    }

    static String parse(String line, String pid) {
        // 连续 14 个不为空格，从第 15 开始 即为 cmd args
        int count = 0;
        boolean flag = true;
        StringBuilder rPid = new StringBuilder();
        StringBuilder args = null;
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) != ' ') {
                if (count == 4 && !flag) {
                    rPid.append(line.charAt(i));
                }

                if (flag) {
                    count++;
                    flag = false;
                    // pid begin
                    if (count == 4) {
                        rPid.append(line.charAt(i));
                    }

                    // pid end
                    if (count == 5) {
                        System.out.println("rPid: " + rPid);
                        if (!pid.equals(rPid.toString())) {
                            System.out.println("this is not target process");
                            break;
                        }
                    }

                    // args begin
                    if (count == 15) {
                        args = new StringBuilder();
                        args.append(line.substring(i));
                        break;
                    }
                }
            } else {
                flag = true;
            }
        }

        if (args != null) {
            System.out.println("args: " + args);
        }

        return args == null ? null : args.toString();
    }

    static void readFromPsInfo(String pid) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/proc/" + pid + "/psinfo"));
        String cmdline = reader.readLine();
        if (cmdline != null) {
            String[] pArgs = cmdline.split("\0");
            System.out.println(Arrays.asList(pArgs));
        }
    }

}
