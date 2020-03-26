package com.proc;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 读取
 * /proc/stat  --》 整个系统的相关信息
 * 或者
 * /proc/${pid}/stat  --》 某个进程的相关信息
 * 或者
 * /proc/${pid}/task/${tid}/stat --》 某个进程中某个线程的相关信息
 * 的相关内容
 *
 * @author xiaoyiluo
 * @createTime 2018/11/14 10:07
 **/
public class StatReader {
    /**
     * 读取进程的状态信息
     *
     * @param pid 进程 id
     * @return 如果进程存在，则返回进程的相关状态信息
     */
    public static TaskStat readProcessStat(int pid) throws IOException {
        return readTaskStat(String.format(PROCESS_STAT_FORMAT, pid));
        //return readTaskStat("/proc/" + pid + "/stat");
    }

    /**
     * 读取进程的某个线程的状态信息
     *
     * @param tid 线程 id
     * @param pid 进程 id
     * @return 如果进程某个线程存在，则返回进程的线程相关状态信息
     */
    public static TaskStat readThreadOfProcessStat(int pid, int tid) throws IOException {
        return readTaskStat(String.format(PROCESS_THREAD_STAT_FORMAT, pid, tid));
        //return readTaskStat("/proc/" + pid + "/task/" + tid + "/stat");
    }

    /**
     * 读取整个系统的 CPU 相关的信息
     *
     * @return 系统 CPU 的相关信息
     */
    public static CpuStat readSystemCpuStat() throws IOException {
        checkOsVersion();
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(SYSTEM_STAT_FORMAT);
            byte[] buf = new byte[1024];
            int readCnt = fis.read(buf);
            fis.close();
            for(int i= 0;i < readCnt; ++i){
                if(buf[i] == '\n'){
                    readCnt = i;
                    break;
                }
            }
            String stateLine = new String(buf, 0, readCnt, Charset.defaultCharset());
            /*eader = new BufferedReader(new FileReader(new File(SYSTEM_STAT_FORMAT)));
            // cpu  160520932 746 104591280 380289150 222805 0 292159 30466 0 0
            String stateLine = reader.readLine();*/
            /*stateLine = stateLine.substring(3).trim(); // 过滤掉 cpu
            String[] props = stateLine.split(" ");
            if (props.length >= 5) {
                CpuStat c = new CpuStat();
                // 用 BigInteger 的原因是，C 语言中的 unsigned long 在 java 中无法表示
                c.user = new BigInteger(props[0]).longValue();
                c.nice = new BigInteger(props[1]).longValue();
                c.system = new BigInteger(props[2]).longValue();
                c.idle = new BigInteger(props[3]).longValue();
                c.iowait = new BigInteger(props[4]).longValue();

                // since Linux 2.6.0-test4
                if (props.length >= 7) {
                    c.irq = new BigInteger(props[5]).longValue();
                    c.softirq = new BigInteger(props[6]).longValue();
                }

                // since Linux 2.6.11
                if (props.length >= 8) {
                    c.steal = new BigInteger(props[7]).longValue();
                }

                // since Linux 2.6.24
                if (props.length >= 9) {
                    c.guest = new BigInteger(props[8]).longValue();
                }

                // (since Linux 2.6.33)
                if (props.length >= 10) {
                    c.guestNice = new BigInteger(props[9]).longValue();
                }

                return c;
            }*/
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return null;
    }

    /**
     * 读取状态文件
     *
     * @param filePath 状态文件路径
     * @return 返回读取结果
     */
    private static TaskStat readTaskStat(String filePath) throws IOException {
        checkOsVersion();

        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buf = new byte[1024];
            int readCnt = fis.read(buf);
            fis.close();
            String stateLine = new String(buf, 0, readCnt, Charset.defaultCharset());
            /*reader = new BufferedReader(new FileReader(new File(filePath)));
            String stateLine = reader.readLine();*/
            /*String[] props = stateLine.split(" ");
            if (props.length >= 41) {
                TaskStat t = new TaskStat();
                t.pid = new BigInteger(props[0]).intValue(); // 1
                t.comm = props[1];
                t.state = props[2].charAt(0);
                t.ppid = new BigInteger(props[3]).intValue();
                t.pgrp = new BigInteger(props[4]).intValue();
                t.session = new BigInteger(props[5]).intValue();
                t.ttyNr = new BigInteger(props[6]).intValue();
                t.tpgid = new BigInteger(props[7]).intValue();
                t.flags = new BigInteger(props[8]).intValue();
                t.minflt = new BigInteger(props[9]).intValue(); // 10

                t.cminflt = new BigInteger(props[10]).longValue(); // 11
                t.majflt = new BigInteger(props[11]).longValue();
                t.cmajflt = new BigInteger(props[12]).longValue();
                t.utime = new BigInteger(props[13]).longValue();
                t.stime = new BigInteger(props[14]).longValue();
                t.cutime = new BigInteger(props[15]).longValue();
                t.cstime = new BigInteger(props[16]).longValue();
                t.priority = new BigInteger(props[17]).longValue();
                t.nice = new BigInteger(props[18]).longValue();
                t.numThreads = new BigInteger(props[19]).longValue(); // 20

                t.itrealValue = new BigInteger(props[20]).longValue();// 21
                t.starttime = new BigInteger(props[21]).longValue();
                t.vsize = new BigInteger(props[22]).longValue();
                t.rss = new BigInteger(props[23]).longValue();
                t.rsslim = new BigInteger(props[24]).longValue();
                t.startcode = new BigInteger(props[25]).longValue();
                t.endcode = new BigInteger(props[26]).longValue();
                t.startstack = new BigInteger(props[27]).longValue();
                t.kstkesp = new BigInteger(props[28]).longValue();
                t.kstkeip = new BigInteger(props[29]).longValue(); // 30

                t.signal = new BigInteger(props[30]).longValue();// 31
                t.blocked = new BigInteger(props[31]).longValue();
                t.sigignore = new BigInteger(props[32]).longValue();
                t.sigcatch = new BigInteger(props[33]).longValue();
                t.wchan = new BigInteger(props[34]).longValue();
                t.nswap = new BigInteger(props[35]).longValue();
                t.cnswap = new BigInteger(props[36]).longValue();
                t.exitSignal = new BigInteger(props[37]).intValue();
                t.processor = new BigInteger(props[38]).intValue();
                t.rtPriority = new BigInteger(props[39]).intValue(); // 40
                t.policy = new BigInteger(props[40]).intValue(); // 41

                // since Linux 2.6.18
                if (props.length > 41) {
                    t.delayacctBlkioTicks = new BigInteger(props[41]).longValue();
                }

                // since Linux 2.6.24
                if (props.length > 43) {
                    t.guestTime = new BigInteger(props[42]).longValue();
                    t.cguestTime = new BigInteger(props[43]).longValue();
                }

                // since Linux 3.3
                if (props.length > 46) {
                    t.startData = new BigInteger(props[44]).longValue();
                    t.endData = new BigInteger(props[45]).longValue();
                    t.startBrk = new BigInteger(props[46]).longValue();
                }

                // since Linux 3.5
                *//*if (props.length > 51) {
                    t.argStart = new BigInteger(props[47]).longValue();
                    t.argEnd = new BigInteger(props[48]).longValue();
                    t.envStart = new BigInteger(props[49]).longValue();
                    t.envEnd = new BigInteger(props[50]).longValue();
                    t.exitCode = new BigInteger(props[51]).intValue(); // 52
                }*//*
                return t;
            }*/
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return null;
    }

    private static boolean checkOsVersion() {
        OsInfo osInfo = OsInfo.instance();

        if (osInfo.isLinux() && (osInfo.getMajorVersion() >= 2 && osInfo.getMinorVersion() >= 6 || osInfo.getMajorVersion() >= 3))
            return true;

        throw new UnSupportedOsVersionException("Unsupported OS: " + OsInfo.instance()
                + ", only Linux 2.6.0 or above is supported.");
    }

    public static final String SYSTEM_STAT_FORMAT = "/proc/stat";
    public static final String PROCESS_STAT_FORMAT = "/proc/%d/stat";
    public static final String PROCESS_THREAD_STAT_FORMAT = "/proc/%d/task/%d/stat";
}
