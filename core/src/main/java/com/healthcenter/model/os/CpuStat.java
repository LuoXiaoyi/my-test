package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * kernel/system statistics.  Varies with architecture.  Common
 * entries include:
 *
 * cpu 10132153 290696 3084719 46828483 16683 0 25195 0 175628 0
 * cpu0 1393280 32966 572056 13343292 6130 0 17875 0 23933 0
 *
 * The amount of time, measured in units of USER_HZ
 * (1/100ths of a second on most architectures, use
 * sysconf(_SC_CLK_TCK) to obtain the right value), that
 * the system ("cpu" line) or the specific CPU ("cpuN"
 * line) spent in various states:
 *
 * @author xiaoyiluo
 * @createTime 2020/3/26 18:54
 **/
public class CpuStat implements Serializable {
    private static final long serialVersionUID = 3548249612117036685L;

    /**
     * user  (1) Time spent in user mode.
     */
    long user;

    /**
     * nice   (2) Time spent in user mode with low priority (nice).
     */
    long nice;

    /**
     * system (3) Time spent in system mode.
     */
    long system;

    /**
     * idle   (4) Time spent in the idle task.
     *     This value should be USER_HZ times the second entry in the
     *     /proc/uptime pseudo-file.
     */
    long idle;

    /**
     * iowait (since Linux 2.5.41)
     * (5) Time waiting for I/O to complete.  This
     *     value is not reliable, for the following rea‐
     *     sons:
     *
     *     1. The CPU will not wait for I/O to complete;
     *     iowait is the time that a task is waiting for
     *     I/O to complete.  When a CPU goes into idle
     *     state for outstanding task I/O, another task
     *     will be scheduled on this CPU.
     *
     *     2. On a multi-core CPU, the task waiting for I/O
     *     to complete is not running on any CPU, so the
     *     iowait of each CPU is difficult to calculate.
     *
     *     3. The value in this field may decrease in cer‐
     *     tain conditions.
     */
    long iowait;

    /**
     *  (since Linux 2.6.0-test4)
     *     (6) Time servicing interrupts.
     */
    long  irq;

    /**
     * softirq (since Linux 2.6.0-test4)
     *     (7) Time servicing softirqs.
     */
    long softirq;

    /**
     * steal (since Linux 2.6.11)
     *     (8) Stolen time, which is the time spent in
     *     other operating systems when running in a virtu‐alized environment
     */
    long steal;

    /**
     * guest (since Linux 2.6.24)
     *     (9) Time spent running a virtual CPU for guest
     *     operating systems under the control of the Linux kernel.
     */
    long guest;

    /**
     * guest_nice (since Linux 2.6.33)
     *     (10) Time spent running a niced guest (virtual
     *     CPU for guest operating systems under the con‐trol of the Linux kernel).
     */
    long guestNice;

    public long getUser() {
        return user;
    }

    public long getNice() {
        return nice;
    }

    public long getSystem() {
        return system;
    }

    public long getIdle() {
        return idle;
    }

    public long getIowait() {
        return iowait;
    }

    public long getIrq() {
        return irq;
    }

    public long getSoftirq() {
        return softirq;
    }

    public long getSteal() {
        return steal;
    }

    public long getGuest() {
        return guest;
    }

    public long getGuestNice() {
        return guestNice;
    }

    @Override
    public String toString() {
        return "CpuStat{" +
                "user=" + user +
                ", nice=" + nice +
                ", system=" + system +
                ", idle=" + idle +
                ", iowait=" + iowait +
                ", irq=" + irq +
                ", softirq=" + softirq +
                ", steal=" + steal +
                ", guest=" + guest +
                ", guestNice=" + guestNice +
                '}';
    }
}
