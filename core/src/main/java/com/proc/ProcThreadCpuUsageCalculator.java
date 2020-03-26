package com.proc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/15 15:34
 **/
public class ProcThreadCpuUsageCalculator {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 2) {
            long interval = Long.valueOf(args[1]);
            ProcThreadCpuUsageCalculator calculator = new ProcThreadCpuUsageCalculator(Integer.parseInt(args[0]));
            while (true) {
                Thread.sleep(interval);
                Map<Long, TaskCpuUsage> result = calculator.calculateCpuUsage();
                System.out.println("pid result: \n" + result);
            }
        }
    }

    /**
     * 对某个进程下的所有线程进行 CPU 利用率的计算
     *
     * @param pid 进程 id
     */
    public ProcThreadCpuUsageCalculator(int pid) {
        this.pid = pid;
        taskParentDir = "/proc/" + pid + "/task";
        fillAllThreadsInfo();
    }

    private void fillAllThreadsInfo() {
        // logger.info("begin to fill all threads info of pid: {}", this.pid);
        File taskParentFile = new File(taskParentDir);
        if (taskParentFile.exists()) {
            /**
             * 得到所有的线程 id，接着就是一个一个读线程的状态文件，有可能在读的过程中会出现失败，
             * 因为某些线程在读的过程中就退出了，所以需要 catch 住每个读取的过程中的异常，防止因为某一个失败，而导致所有的都失败
             */
            File[] subThreadsDirs = taskParentFile.listFiles();

            if (subThreadsDirs != null) {
                for (File thread : subThreadsDirs) {
                    try {
                        CpuStat cpuStat = StatReader.readSystemCpuStat();
                        int tid = Integer.parseInt(thread.getName());
                        TaskStat taskStat = null;
                        if (tid != this.pid) {
                            taskStat = StatReader.readThreadOfProcessStat(pid, tid);
                        } else {
                            taskStat = StatReader.readProcessStat(pid);
                        }

                        cpuSampleTimes.put((long) tid,
                                new ValueGroup(CpuUsageUtil.sysCpuTime(cpuStat),
                                        CpuUsageUtil.taskUserCpuTime(taskStat, false),
                                        CpuUsageUtil.taskSysCpuTime(taskStat, false)));
                    } catch (Throwable ignore) {
                    }
                }
            }
        }
    }

    /**
     * 返回计算结果，结果为轻量级进程 id -- cpu 使用率的 map，所以在外面获取某个线程的 CPU 使用率时，需要使用 nativeThreadId
     *
     * @return 该 map 为 nativeThreadId <--> cpu 使用率，严格意义上来说，若某个线程的 CPU 使用率 < 0.0001 时，将直接被认为是 0
     * @throws IOException
     */
    public Map<Long, TaskCpuUsage> calculateCpuUsage() throws IOException {
        // logger.debug("begin to calculate Cpu Usage for all threads info of pid: {}", this.pid);
        File taskParentFile = new File(taskParentDir);
        if (taskParentFile.exists()) {
            Map<Long, TaskCpuUsage> result = new HashMap<>();
            /**
             * 得到所有的线程 id，接着就是一个一个读线程的状态文件，有可能在读的过程中会出现失败，
             * 因为某些线程在读的过程中就退出了，所以需要 catch 住每个读取的过程中的异常，防止因为某一个失败，而导致所有的都失败
             */
            File[] subThreadsDirs = taskParentFile.listFiles();

            for (File thread : subThreadsDirs) {
                CpuStat cpuStat = StatReader.readSystemCpuStat();
                //System.out.println("CPUStat: " + cpuStat);
                try {
                    int tid = Integer.parseInt(thread.getName());
                    TaskStat taskStat = null;
                    if (tid != this.pid) {
                        taskStat = StatReader.readThreadOfProcessStat(pid, tid);
                    } else {
                        taskStat = StatReader.readProcessStat(pid);
                    }

                    if(cpuStat == null || taskStat == null) continue;

                    //System.out.println("TaskStat: " + taskStat);
                    TaskCpuUsage tcu = calculateCPUTime((long) tid,
                            CpuUsageUtil.sysCpuTime(cpuStat),
                            CpuUsageUtil.taskUserCpuTime(taskStat, false),
                            CpuUsageUtil.taskSysCpuTime(taskStat, false));

                    if (tcu == null) continue;

                    // 在极少数情况下（如两次计算的间隔时间过短），r的值可能会计算出来大于1，对于这种情况，为了避免外部的疑惑，直接赋值成为0.9999
                    if (tid != this.pid && tcu.totalCpu > 1) {
                        tcu.totalCpu = 0.9999;
                    }
                    // 对于少于 0.0001 的结果，直接认为是0，即不反回结果
                    if (tcu.totalCpu > 0.0000999999999)
                        result.put((long) tid, tcu);
                } catch (Throwable ignore) {
                    ignore.printStackTrace();
                }
            }

            return result;
        }

        return null;
    }

    private TaskCpuUsage calculateCPUTime(Long threadId, long nowCpuTime,
                                          long nowThreadUserCpuTime, long nowThreadSysCpuTime) {
        ValueGroup p = cpuSampleTimes.get(threadId);
        if (p != null) {
            if (nowCpuTime > p.lastCpuTime
                    && nowThreadUserCpuTime > p.lastThreadUserCpuTime
                    && nowThreadSysCpuTime > p.lastThreadSysCpuTime) {
                TaskCpuUsage tcu = new TaskCpuUsage();
                tcu.userCpu = AVAILABLE_PROCESSORS *
                        (double) (nowThreadUserCpuTime - p.lastThreadUserCpuTime) /
                        (double) (nowCpuTime - p.lastCpuTime);
                tcu.sysCpu = AVAILABLE_PROCESSORS *
                        (double) (nowThreadSysCpuTime - p.lastThreadSysCpuTime) /
                        (double) (nowCpuTime - p.lastCpuTime);
                tcu.totalCpu = tcu.sysCpu + tcu.userCpu;

                return tcu;
            }
        } else {
            if (nowCpuTime > 0 && nowThreadUserCpuTime > 0 && nowThreadSysCpuTime > 0)
                cpuSampleTimes.put(threadId,
                        new ValueGroup(nowCpuTime, nowThreadUserCpuTime, nowThreadSysCpuTime));
        }

        return null;
    }

    /**
     * 清空计算器
     */
    public void clear() {
        cpuSampleTimes.clear();
    }

    /**
     * 当某个线程不在的时候，从缓存中去掉线程
     *
     * @param threadId 线程 id
     */
    public void remove(Long threadId) {
        cpuSampleTimes.remove(threadId);
    }

    // 用于保存上次的某个线程的 CPU 使用时间
    private final Map<Long, ValueGroup> cpuSampleTimes = new ConcurrentHashMap<>();
    private final int pid;
    private final String taskParentDir;

    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static class ValueGroup {
        public ValueGroup(long c, long tu, long ts) {
            lastCpuTime = c;
            lastThreadUserCpuTime = tu;
            lastThreadSysCpuTime = ts;
        }

        long lastCpuTime;
        long lastThreadUserCpuTime;
        long lastThreadSysCpuTime;
    }
}
