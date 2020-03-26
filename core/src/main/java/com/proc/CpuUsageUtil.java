package com.proc;

import java.io.IOException;

/**
 * CPU 使用率计算器
 * http://www.blogjava.net/fjzag/articles/317773.html
 *
 * @author xiaoyiluo
 * @createTime 2018/11/14 13:16
 **/
public class CpuUsageUtil {

    /**
     * Usage Example.
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("os.name: " + System.getProperties().get("os.name"));
        System.out.println("os.arch: " + System.getProperties().get("os.arch"));
        System.out.println("os.version: " + System.getProperties().get("os.version"));
        System.out.println(Runtime.getRuntime().availableProcessors());

        int pid = 0, tid = 0;
        if (args.length == 1) {
            pid = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            pid = Integer.parseInt(args[0]);
            tid = Integer.parseInt(args[1]);
        }

        System.out.println("pid: " + pid + ", tid: " + tid);

        if (pid != 0 && tid != 0) {
            while (true) {
                TaskStat oldStat = StatReader.readThreadOfProcessStat(pid, tid);
                CpuStat oldCpuStat = StatReader.readSystemCpuStat();
                Thread.sleep(3000);
                TaskStat newStat = StatReader.readThreadOfProcessStat(pid, tid);
                CpuStat newCpuStat = StatReader.readSystemCpuStat();
                double result = calculatorCpuUsage(newStat, newCpuStat, oldStat, oldCpuStat, false);
                System.out.println("pid: " + pid + ", tid: " + tid + ", result: " + result);
            }
        } else if (pid != 0) {
            while (true) {
                TaskStat oldStat = StatReader.readProcessStat(pid);
                CpuStat oldCpuStat = StatReader.readSystemCpuStat();
                Thread.sleep(3000);
                TaskStat newStat = StatReader.readProcessStat(pid);
                CpuStat newCpuStat = StatReader.readSystemCpuStat();
                double result = calculatorCpuUsage(newStat, newCpuStat, oldStat, oldCpuStat, true);
                System.out.println("pid: " + pid + " result: " + result);
            }
        }
    }

    /**
     * 进程或者线程的 CPU 使用率的计算，计算的大概思路是：
     * 将最新的 CPU 数据和之前一次的 CPU 数据进行差值计算，然后将线程或者进程的 CPU 差值除以系统 CPU 的差值，最终乘以 CPU 的总核心数
     * 从而得到最终的计算结果
     * @param newTaskStat 最近的进程（或者线程）状态信息
     * @param newCpuStat 最近的系统 CPU 状态信息
     * @param oldTaskStat 之前一次的进程（或者线程）状态信息
     * @param oldCpuStat 之前一次的系统 CPU 状态信息
     * @param isProcess 标识是计算进程的 CPU 使用率还是线程的，如果为 true，则为计算进程，否则计算线程，需要注意的是，计算之前传入的
     *                  相关值必须是线程或者进程的相关值
     * @return 返回最终的计算结果
     */
    public static double calculatorCpuUsage(TaskStat newTaskStat, CpuStat newCpuStat,
                                            TaskStat oldTaskStat, CpuStat oldCpuStat, boolean isProcess) {
        long newTaskCpuTime = taskCpuTime(newTaskStat, isProcess);
        long oldTaskCpuTime = taskCpuTime(oldTaskStat, isProcess);

        long newSysCpuTime = sysCpuTime(newCpuStat);
        long oldSysCpuTime = sysCpuTime(oldCpuStat);

        return AVAILABLE_PROCESSORS * (double) (newTaskCpuTime - oldTaskCpuTime) / (double) (newSysCpuTime - oldSysCpuTime);
    }

    /**
     * 计算任务的总 CPU 时间（进程+线程）
     *
     * @param taskStat  任务的状态对象
     * @param isProcess 是否是进程，true 为进程，否则为线程
     * @return 任务的总时间
     */
    public static long taskCpuTime(TaskStat taskStat, boolean isProcess) {
        if (isProcess) {
            return taskStat.utime + taskStat.cutime + taskStat.stime + taskStat.cstime;
        } else {
            // 线程不需要 cutime + cstime，因为他们不会有子进程
            return taskStat.utime + taskStat.stime;
        }
    }

    /**
     * 计算任务的总 CPU 时间（进程+线程）
     *
     * @param taskStat  任务的状态对象
     * @param isProcess 是否是进程，true 为进程，否则为线程
     * @return 任务的总时间
     */
    public static long taskUserCpuTime(TaskStat taskStat, boolean isProcess) {
        if (isProcess) {
            return taskStat.utime + taskStat.cutime;
        } else {
            // 线程不需要 cutime，因为他们不会有子进程
            return taskStat.utime;
        }
    }

    /**
     * 计算任务的总 CPU 时间（进程+线程）
     *
     * @param taskStat  任务的状态对象
     * @param isProcess 是否是进程，true 为进程，否则为线程
     * @return 任务的总时间
     */
    public static long taskSysCpuTime(TaskStat taskStat, boolean isProcess) {
        if (isProcess) {
            return taskStat.stime + taskStat.cstime;
        } else {
            // 线程不需要 cutime + cstime，因为他们不会有子进程
            return taskStat.stime;
        }
    }

    /**
     * 计算总的 cpu 使用时间
     *
     * @param cpuStat cpu 状态对象
     * @return 返回总时间
     */
    public static long sysCpuTime(CpuStat cpuStat) {
        return cpuStat.guestNice + cpuStat.guest + cpuStat.steal + cpuStat.softirq + cpuStat.irq +
                cpuStat.iowait + cpuStat.idle + cpuStat.system + cpuStat.nice + cpuStat.user;
    }

    /**
     * CPU 的核心数
     */
    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
}
