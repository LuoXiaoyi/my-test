import org.hyperic.sigar.*;
import org.hyperic.sigar.cmd.Ps;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Arrays;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/6/10 21:58
 * @Version 1.0
 **/
public class SigarTest {
    static {
        final String sigarPath = "/root/test/xowl/dlibs/libsigar-sparc64-solaris.so";
        System.load(sigarPath);
        System.out.println("Success load: " + sigarPath);
    }

    public static void main(String[] args) throws Exception {
        int pid = currentProcessId(0);
        System.out.println("currentProcessId: " + pid);
        Sigar sigar = new Sigar();
        CpuPerc cpuPerc = sigar.getCpuPerc();
        System.out.println("cpuPerc: " + cpuPerc);

        ProcCpu cpu = sigar.getProcCpu(pid);
        System.out.println("ProcCpu: " + cpu);

        double[] load = sigar.getLoadAverage();
        System.out.println("load avg: " + Arrays.asList(load));

        Mem mem = sigar.getMem();
        System.out.println("Mem: " + mem);
        Swap swap = sigar.getSwap();
        System.out.println("swap: " + swap);

        String[] inters = sigar.getNetInterfaceList();
        System.out.println("interfaces: " + Arrays.asList(inters));
        for (String name : inters) {
            System.out.println("getNetInterfaceConfig: " + sigar.getNetInterfaceConfig(name));
            System.out.println("getNetInterfaceStat: " + sigar.getNetInterfaceStat(name));
        }

        FileSystem[] fss = sigar.getFileSystemList();
        System.out.println("getFileSystemList: " + Arrays.asList(fss));

        for (FileSystem fs : fss) {
            System.out.println("getFileSystemUsage: " + sigar.getFileSystemUsage(fs.getDirName()));
        }

        Ps ps = new Ps();
        System.out.println("getInfo: " + ps.getInfo(sigar, pid));
    }

    /**
     * 获取当前的进程 ID
     *
     * @param defaultId 如果获取失败，则返回默认值
     * @return 返回当前进程 ID
     */
    public static int currentProcessId(int defaultId) {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName();

            int idx;
            if ((idx = name.indexOf('@')) < 0) {
                System.out.println("get process id failed, default id " + defaultId + " will be used.");
                return defaultId;
            }

            return Integer.parseInt(name.substring(0, idx));
        } catch (Throwable t) {
            System.out.println("get process id failed, default id " + defaultId + "  will be used.");
            t.printStackTrace();
            return defaultId;
        }
    }
}
