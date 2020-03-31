package com.healthcenter.model.jvm;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiluo
 * @date 2020/3/31 21:41
 * @Version 1.0
 **/
public class ThreadInfo implements Serializable {
    private static final long serialVersionUID = -2372058742636505291L;
    private int threadCount;
    private List<DeadLockThread> deadLocks;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public List<DeadLockThread> getDeadLocks() {
        return deadLocks;
    }

    public void setDeadLocks(List<DeadLockThread> deadLocks) {
        this.deadLocks = deadLocks;
    }

    public static class DeadLockThread implements Serializable {
        private static final long serialVersionUID = -5658003474190864997L;
        private int tid;
        /**
         * native thread id
         */
        private int nTid;
        private String threadName;
        private List<String> stackTraceLines;

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getnTid() {
            return nTid;
        }

        public void setnTid(int nTid) {
            this.nTid = nTid;
        }

        public String getThreadName() {
            return threadName;
        }

        public void setThreadName(String threadName) {
            this.threadName = threadName;
        }

        public List<String> getStackTraceLines() {
            return stackTraceLines;
        }

        public void setStackTraceLines(List<String> stackTraceLines) {
            this.stackTraceLines = stackTraceLines;
        }
    }
}
