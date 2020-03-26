package com.proc;

/**
 * http://man7.org/linux/man-pages/man5/proc.5.html
 *
 *   Status information about the process.  This is used by ps(1).
 *     It is defined in the kernel source file fs/proc/array.c.
 *
 *     The fields, in order, with their proper scanf(3) format speci‐
 *     fiers, are listed below.  Whether or not certain of these
 *     fields display valid information is governed by a ptrace
 *     access mode PTRACE_MODE_READ_FSCREDS | PTRACE_MODE_NOAUDIT
 *     check (refer to ptrace(2)).  If the check denies access, then
 *     the field value is displayed as 0.  The affected fields are
 *     indicated with the marking [PT].
 *
 * @author xiaoyiluo
 * @createTime 2018/11/14 11:24
 **/
public class TaskStat {

    /**
     * (1) pid  %d
     *     The process ID.
     */
    int pid;

    /**
     * (2) comm  %s
     *     The filename of the executable, in parentheses.
     *     This is visible whether or not the executable is
     *     swapped out.
     */
    String comm;

    /**
     * (3) state  %c
     *     One of the following characters, indicating process
     *     state:
     *
     *     R  Running
     *
     *     S  Sleeping in an interruptible wait
     *
     *     D  Waiting in uninterruptible disk sleep
     *
     *     Z  Zombie
     *
     *     T  Stopped (on a signal) or (before Linux 2.6.33)
     *     trace stopped
     *
     *     t  Tracing stop (Linux 2.6.33 onward)
     *
     *     W  Paging (only before Linux 2.6.0)
     *
     *     X  Dead (from Linux 2.6.0 onward)
     *
     *     x  Dead (Linux 2.6.33 to 3.13 only)
     *
     *     K  Wakekill (Linux 2.6.33 to 3.13 only)
     *
     *     W  Waking (Linux 2.6.33 to 3.13 only)
     *
     *     P  Parked (Linux 3.9 to 3.13 only)
     */
    char state;

    /**
     * (4) ppid  %d
     *     The PID of the parent of this process.
     */
    int ppid;

    /**
     * (5) pgrp  %d
     *     The process group ID of the process.
     */
    int pgrp;

    /**
     * (6) session  %d
     *      The session ID of the process.
     */
    int session;

    /**
     *  (7) tty_nr  %d
     *     The controlling terminal of the process.  (The minor
     *     device number is contained in the combination of
     *     bits 31 to 20 and 7 to 0; the major device number is
     *     in bits 15 to 8.)
      */
    int ttyNr;

    /**
     * (8) tpgid  %d
     *     The ID of the foreground process group of the con‐
     *     trolling terminal of the process.
     */
    int tpgid;

    /**
     * (9) flags  %u
     *     The kernel flags word of the process.  For bit mean‐
     *     ings, see the PF_* defines in the Linux kernel
     *     source file include/linux/sched.h.  Details depend
     *     on the kernel version.
     *     The format for this field was %lu before Linux 2.6.
     */
    int flags;

    /**
     * (10) minflt  %lu
     *      The number of minor faults the process has made
     *      which have not required loading a memory page from
     *      disk.
     */
    long minflt;

    /**
     * (11) cminflt  %lu
     *     The number of minor faults that the process's
     *     waited-for children have made.
     */
    long cminflt;

    /**
     * (12) majflt  %lu
     *     The number of major faults the process has made
     *     which have required loading a memory page from disk.
     */
    long majflt;

    /**
     *  (13) cmajflt  %lu
     *     The number of major faults that the process's
     *     waited-for children have made.
     */
    long cmajflt;

    /**
     *  (14) utime  %lu
     *     Amount of time that this process has been scheduled
     *     in user mode, measured in clock ticks (divide by
     *                         sysconf(_SC_CLK_TCK)).  This includes guest time,
     *     guest_time (time spent running a virtual CPU, see
     *             below), so that applications that are not aware of
     *     the guest time field do not lose that time from
     *     their calculations.
     */
    long utime;

    /**
     *  (15) stime  %lu
         Amount of time that this process has been scheduled
         in kernel mode, measured in clock ticks (divide by
         sysconf(_SC_CLK_TCK)).
     */
    long stime;

    /**
     *  (16) cutime  %ld
         Amount of time that this process's waited-for chil‐
         dren have been scheduled in user mode, measured in
         clock ticks (divide by sysconf(_SC_CLK_TCK)).  (See
         also times(2).)  This includes guest time,
         cguest_time (time spent running a virtual CPU, see
         below).
     */
    long cutime;

    /**
     * (17) cstime  %ld
         Amount of time that this process's waited-for chil‐
         dren have been scheduled in kernel mode, measured in
         clock ticks (divide by sysconf(_SC_CLK_TCK)).
     */
    long cstime;

    /**
     *  (18) priority  %ld
     (Explanation for Linux 2.6) For processes running a
     real-time scheduling policy (policy below; see
     sched_setscheduler(2)), this is the negated schedul‐
     ing priority, minus one; that is, a number in the
     range -2 to -100, corresponding to real-time priori‐
     ties 1 to 99.  For processes running under a non-
     real-time scheduling policy, this is the raw nice
     value (setpriority(2)) as represented in the kernel.
     The kernel stores nice values as numbers in the
     range 0 (high) to 39 (low), corresponding to the
     user-visible nice range of -20 to 19.

     Before Linux 2.6, this was a scaled value based on
     the scheduler weighting given to this process.
     */
    long priority;

    /**
     * (19) nice  %ld
     The nice value (see setpriority(2)), a value in the
     range 19 (low priority) to -20 (high priority).
     */
    long nice;

    /**
     * (20) num_threads  %ld
     *     Number of threads in this process (since Linux 2.6).
     *     Before kernel 2.6, this field was hard coded to 0 as
     *     a placeholder for an earlier removed field.
     */
    long numThreads;

    /**
     * (21) itrealvalue  %ld
     *     The time in jiffies before the next SIGALRM is sent
     *     to the process due to an interval timer.  Since ker‐
     *     nel 2.6.17, this field is no longer maintained, and
     *     is hard coded as 0.
     */
    long itrealValue;

    /**
     * (22) starttime  %llu
     *     The time the process started after system boot.  In
     *     kernels before Linux 2.6, this value was expressed
     *     in jiffies.  Since Linux 2.6, the value is expressed
     *     in clock ticks (divide by sysconf(_SC_CLK_TCK)).
     *
     *     The format for this field was %lu before Linux 2.6.
     */
    long starttime;

    /**
     * (23) vsize  %lu
     *     Virtual memory size in bytes.
     */
    long vsize;

    /**
     * (24) rss  %ld
     *     Resident Set Size: number of pages the process has
     *     in real memory.  This is just the pages which count
     *     toward text, data, or stack space.  This does not
     *     include pages which have not been demand-loaded in,
     *     or which are swapped out.
     */
    long rss;

    /**
     * (25) rsslim  %lu
     *     Current soft limit in bytes on the rss of the
     *     process; see the description of RLIMIT_RSS in
     *     getrlimit(2).
     */
    long rsslim;

    /**
     * (26) startcode  %lu  [PT]
     *     The address above which program text can run.
     */
    long startcode;

    /**
     * (27) endcode  %lu  [PT]
     *     The address below which program text can run.
     */
    long endcode;


    /**
     * (28) startstack  %lu  [PT]
     *     The address of the start (i.e., bottom) of the
     *                         stack.
     */
    long startstack;

    /**
     * (29) kstkesp  %lu  [PT]
     *     The current value of ESP (stack pointer), as found
     *     in the kernel stack page for the process.
     */
    long kstkesp;

    /**
     * (30) kstkeip  %lu  [PT]
     *     The current EIP (instruction pointer).
     */
    long kstkeip;

    /**
     * (31) signal  %lu
         The bitmap of pending signals, displayed as a deci‐
         mal number.  Obsolete, because it does not provide
         information on real-time signals; use
         /proc/[pid]/status instead.
     */
    long signal;

    /**
     * (32) blocked  %lu
     *     The bitmap of blocked signals, displayed as a deci‐
     *     mal number.  Obsolete, because it does not provide
     *     information on real-time signals; use
     *                         /proc/[pid]/status instead.
     */
    long blocked;

    /**
     * (33) sigignore  %lu
     *     The bitmap of ignored signals, displayed as a deci‐
     *     mal number.  Obsolete, because it does not provide
     *     information on real-time signals; use
     *     /proc/[pid]/status instead.
     */
    long sigignore;

    /**
     * (34) sigcatch  %lu
     *     The bitmap of caught signals, displayed as a decimal
     *     number.  Obsolete, because it does not provide
     *     information on real-time signals; use
     *     /proc/[pid]/status instead.
     */
    long sigcatch;

    /**
     * (35) wchan  %lu  [PT]
     *     This is the "channel" in which the process is wait‐
     *     ing.  It is the address of a location in the kernel
     *     where the process is sleeping.  The corresponding
     *     symbolic name can be found in /proc/[pid]/wchan.
     */
    long wchan;

    /**
     * (36) nswap  %lu
     *     Number of pages swapped (not maintained).
     */
    long nswap;

    /**
     * (37) cnswap  %lu
     *     Cumulative nswap for child processes (not main‐
     *             tained).
     */
    long cnswap;

    /**
     * (38) exit_signal  %d  (since Linux 2.1.22)
     *     Signal to be sent to parent when we die.
     */
    int exitSignal;

    /**
     *  (39) processor  %d  (since Linux 2.2.8)
     *     CPU number last executed on.
     */
    int processor;

    /**
     * (40) rt_priority  %u  (since Linux 2.5.19)
     *     Real-time scheduling priority, a number in the range
     *                         1 to 99 for processes scheduled under a real-time
     *     policy, or 0, for non-real-time processes (see sched_setscheduler(2)).
     */
    int rtPriority;

    /**
     * (41) policy  %u  (since Linux 2.5.19)
     *     Scheduling policy (see sched_setscheduler(2)).
     *     Decode using the SCHED_* constants in linux/sched.h.
     *
     *     The format for this field was %lu before Linux
     *                         2.6.22.
     */
    int policy;

    /**
     * (42) delayacct_blkio_ticks  %llu  (since Linux 2.6.18)
     *     Aggregated block I/O delays, measured in clock ticks
     *             (centiseconds).
     */
    long delayacctBlkioTicks;

    /**
     * (43) guest_time  %lu  (since Linux 2.6.24)
     *     Guest time of the process (time spent running a vir‐
     *             tual CPU for a guest operating system), measured in
     *     clock ticks (divide by sysconf(_SC_CLK_TCK)).
     */
    long guestTime;

    /**
     * (44) cguest_time  %ld  (since Linux 2.6.24)
     *     Guest time of the process's children, measured in
     *     clock ticks (divide by sysconf(_SC_CLK_TCK)).
     */
    long cguestTime;

    /**
     * (45) start_data  %lu  (since Linux 3.3)  [PT]
     *     Address above which program initialized and unini‐
     *     tialized (BSS) data are placed.
     */
    long startData;

    /**
     *  (46) end_data  %lu  (since Linux 3.3)  [PT]
     *     Address below which program initialized and unini‐
     *     tialized (BSS) data are placed.
     */
    long endData;

    /**
     * (47) start_brk  %lu  (since Linux 3.3)  [PT]
     *     Address above which program heap can be expanded
     *     with brk(2).
     */
    long startBrk;

    /**
     * (48) arg_start  %lu  (since Linux 3.5)  [PT]
     *     Address above which program command-line arguments
     *             (argv) are placed.
     */
    long argStart;

    /**
     * (49) arg_end  %lu  (since Linux 3.5)  [PT]
     *     Address below program command-line arguments (argv)
     *     are placed.
     */
    long argEnd;

    /**
     * (50) env_start  %lu  (since Linux 3.5)  [PT]
     *     Address above which program environment is placed.
     */
    long envStart;

    /**
     * (51) env_end  %lu  (since Linux 3.5)  [PT]
     *     Address below which program environment is placed.
     */
    long envEnd;

    /**
     * (52) exit_code  %d  (since Linux 3.5)  [PT]
     *     The thread's exit status in the form reported by
     *     waitpid(2).
     */
   int exitCode;

    public int getPid() {
        return pid;
    }

    public String getComm() {
        return comm;
    }

    public char getState() {
        return state;
    }

    public int getPpid() {
        return ppid;
    }

    public int getPgrp() {
        return pgrp;
    }

    public int getSession() {
        return session;
    }

    public int getTtyNr() {
        return ttyNr;
    }

    public int getTpgid() {
        return tpgid;
    }

    public int getFlags() {
        return flags;
    }

    public long getMinflt() {
        return minflt;
    }

    public long getCminflt() {
        return cminflt;
    }

    public long getMajflt() {
        return majflt;
    }

    public long getCmajflt() {
        return cmajflt;
    }

    public long getUtime() {
        return utime;
    }

    public long getStime() {
        return stime;
    }

    public long getCutime() {
        return cutime;
    }

    public long getCstime() {
        return cstime;
    }

    public long getPriority() {
        return priority;
    }

    public long getNice() {
        return nice;
    }

    public long getNumThreads() {
        return numThreads;
    }

    public long getItrealValue() {
        return itrealValue;
    }

    public long getStarttime() {
        return starttime;
    }

    public long getVsize() {
        return vsize;
    }

    public long getRss() {
        return rss;
    }

    public long getRsslim() {
        return rsslim;
    }

    public long getStartcode() {
        return startcode;
    }

    public long getEndcode() {
        return endcode;
    }

    public long getStartstack() {
        return startstack;
    }

    public long getKstkesp() {
        return kstkesp;
    }

    public long getKstkeip() {
        return kstkeip;
    }

    public long getSignal() {
        return signal;
    }

    public long getBlocked() {
        return blocked;
    }

    public long getSigignore() {
        return sigignore;
    }

    public long getSigcatch() {
        return sigcatch;
    }

    public long getWchan() {
        return wchan;
    }

    public long getNswap() {
        return nswap;
    }

    public long getCnswap() {
        return cnswap;
    }

    public long getExitSignal() {
        return exitSignal;
    }

    public int getProcessor() {
        return processor;
    }

    public int getRtPriority() {
        return rtPriority;
    }

    public int getPolicy() {
        return policy;
    }

    public long getDelayacctBlkioTicks() {
        return delayacctBlkioTicks;
    }

    public long getGuestTime() {
        return guestTime;
    }

    public long getCguestTime() {
        return cguestTime;
    }

    public long getStartData() {
        return startData;
    }

    public long getEndData() {
        return endData;
    }

    public long getStartBrk() {
        return startBrk;
    }

    public long getArgStart() {
        return argStart;
    }

    public long getArgEnd() {
        return argEnd;
    }

    public long getEnvStart() {
        return envStart;
    }

    public long getEnvEnd() {
        return envEnd;
    }

    public int getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return "TaskStat{" +
                "pid=" + pid +
                ", comm='" + comm + '\'' +
                ", state=" + state +
                ", ppid=" + ppid +
                ", pgrp=" + pgrp +
                ", session=" + session +
                ", ttyNr=" + ttyNr +
                ", tpgid=" + tpgid +
                ", flags=" + flags +
                ", minflt=" + minflt +
                ", cminflt=" + cminflt +
                ", majflt=" + majflt +
                ", cmajflt=" + cmajflt +
                ", utime=" + utime +
                ", stime=" + stime +
                ", cutime=" + cutime +
                ", cstime=" + cstime +
                ", priority=" + priority +
                ", nice=" + nice +
                ", numThreads=" + numThreads +
                ", itrealValue=" + itrealValue +
                ", starttime=" + starttime +
                ", vsize=" + vsize +
                ", rss=" + rss +
                ", rsslim=" + rsslim +
                ", startcode=" + startcode +
                ", endcode=" + endcode +
                ", startstack=" + startstack +
                ", kstkesp=" + kstkesp +
                ", kstkeip=" + kstkeip +
                ", signal=" + signal +
                ", blocked=" + blocked +
                ", sigignore=" + sigignore +
                ", sigcatch=" + sigcatch +
                ", wchan=" + wchan +
                ", nswap=" + nswap +
                ", cnswap=" + cnswap +
                ", exitSignal=" + exitSignal +
                ", processor=" + processor +
                ", rtPriority=" + rtPriority +
                ", policy=" + policy +
                ", delayacctBlkioTicks=" + delayacctBlkioTicks +
                ", guestTime=" + guestTime +
                ", cguestTime=" + cguestTime +
                ", startData=" + startData +
                ", endData=" + endData +
                ", startBrk=" + startBrk +
                ", argStart=" + argStart +
                ", argEnd=" + argEnd +
                ", envStart=" + envStart +
                ", envEnd=" + envEnd +
                ", exitCode=" + exitCode +
                '}';
    }
}
