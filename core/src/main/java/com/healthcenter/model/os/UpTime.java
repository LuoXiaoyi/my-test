package com.healthcenter.model.os;

/**
 * @author xiluo
 * @ClassName
 * @description <pre>
 * /proc/uptime
 *      This  file  contains two numbers: the uptime of the system (seconds), and the amount of time spent in
 *      idle process (seconds).
 *
 * /proc/loadavg
 *               The first three fields in this file are load average figures giving the number of  jobs  in  the  run
 *               queue  (state  R) or waiting for disk I/O (state D) averaged over 1, 5, and 15 minutes.  They are the
 *               same as the load average numbers given by uptime(1) and other programs.  The fourth field consists of
 *               two  numbers separated by a slash (/).  The first of these is the number of currently runnable kernel
 *               scheduling entities (processes, threads).  The value after the slash is the number of kernel schedul‐
 *               ing  entities that currently exist on the system.  The fifth field is the PID of the process that was
 *               most recently created on the system.
 *
 * /var/run/utmp
 *              Users
 *              https://blog.csdn.net/u011641885/article/details/46958613
 *
 *  eg:
 *  $ uptime
 *  20:41:30 up 8 days,  7:36,  7 users,  load average: 0.00, 0.07, 0.13
 * </pre>
 * @date 2020/3/26 20:14
 * @Version 1.0
 **/
public class UpTime {
    private long currentMs;
    private long uptimeSecs;

    /**
     * user number
     */
    private int userNbr;

    private double load1min;
    private double load5min;
    private double load15min;

    /**
     * The first of these is the number of currently runnable kernel scheduling entities (processes, threads).
     */
    private int runnableProcessNbr;

    /**
     * The value after the slash is the number of kernel scheduling  entities that currently exist on the system.
     */
    private int currentTotalProcessNbr;

    /**
     * The fifth field is the PID of the process that was most recently created on the system.
     */
    private int recentlyPid;

    public long getCurrentMs() {
        return currentMs;
    }

    public void setCurrentMs(long currentMs) {
        this.currentMs = currentMs;
    }

    public long getUptimeSecs() {
        return uptimeSecs;
    }

    public void setUptimeSecs(long uptimeSecs) {
        this.uptimeSecs = uptimeSecs;
    }

    public int getUserNbr() {
        return userNbr;
    }

    public void setUserNbr(int userNbr) {
        this.userNbr = userNbr;
    }

    public double getLoad1min() {
        return load1min;
    }

    public void setLoad1min(double load1min) {
        this.load1min = load1min;
    }

    public double getLoad5min() {
        return load5min;
    }

    public void setLoad5min(double load5min) {
        this.load5min = load5min;
    }

    public double getLoad15min() {
        return load15min;
    }

    public void setLoad15min(double load15min) {
        this.load15min = load15min;
    }

    public int getRunnableProcessNbr() {
        return runnableProcessNbr;
    }

    public void setRunnableProcessNbr(int runnableProcessNbr) {
        this.runnableProcessNbr = runnableProcessNbr;
    }

    public int getCurrentTotalProcessNbr() {
        return currentTotalProcessNbr;
    }

    public void setCurrentTotalProcessNbr(int currentTotalProcessNbr) {
        this.currentTotalProcessNbr = currentTotalProcessNbr;
    }

    public int getRecentlyPid() {
        return recentlyPid;
    }

    public void setRecentlyPid(int recentlyPid) {
        this.recentlyPid = recentlyPid;
    }
}
