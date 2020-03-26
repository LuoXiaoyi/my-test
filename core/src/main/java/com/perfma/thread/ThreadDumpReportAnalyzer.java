package com.perfma.thread;

import com.perfma.thread.model.MonitorInfoModel;
import com.perfma.thread.model.ThreadDumpMetadata;
import com.perfma.thread.model.ThreadInfoModel;

public class ThreadDumpReportAnalyzer {

    public static void analyzeDump(ThreadDumpMetadata metadata) {
        StringBuffer statData = new StringBuffer();
        int deadlocks = metadata.getDeadLockNum();
        int threadCount = metadata.getThreads().size();
        int waiting = metadata.getBlockings().size();
        int sleeping = metadata.getSleepings().size();

        int overallThreadsWaitingWithoutLocks = metadata
            .getOverallThreadsWaitingWithoutLocksCount();
        int monitorsWithoutLocksCount = metadata.getMonitorsWithoutLocks().size();

        int max_stack_depth_num = metadata.getMaxStackDepth();
        int max_block_num = metadata.getMaxBlockNum();
        ThreadInfoModel max_block_thread = metadata.getMaxBlockNumThread();
        ThreadInfoModel max_stack_depth_thread = metadata.getMaxStackDepthThread();
        MonitorInfoModel max_block_monitor = metadata.getMaxBlockNumMonitor();

        statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
        statData.append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System "
                        + "><p>【栈最深的线程(栈深" + max_stack_depth_num + ")】--><br>        线程对象:"
                        + "<a href=\"/thread/threads.htm?file_name=" + metadata.getFileName()
                        + "&thread_type=all_thread&pattern=" + max_stack_depth_thread.getName()
                        + "\" target=\"_blank\">" + max_stack_depth_thread.getTitle()
                        + "</a>.</p><br></td></tr>");

        if (max_block_thread != null) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData.append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System "
                            + "><p>【阻塞线程最多的线程(阻塞了" + max_block_num + ")】--><br>        线程对象:"
                            + "<a href=\"/thread/threads.htm?file_name=" + metadata.getFileName()
                            + "&thread_type=all_thread&pattern=" + max_block_thread.getName()
                            + "\" target=\"_blank\">" + max_block_thread.getTitle()
                            + "</a><br>        监控对象:" + "<a href=\"/thread/monitors.htm?file_name="
                            + metadata.getFileName() + "&monitor_type=monitors&pattern="
                            + max_block_monitor.getAddress() + "\" target=\"_blank\">"
                            + max_block_monitor.getTitle() + "</a>.</p><br></td></tr>");
        }

        if ((deadlocks == 0) && (threadCount > 0) && ((waiting / (threadCount / 100.0)) > 10.0)) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData
                .append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System "
                        + "><p><a href=\"/thread/threads.htm?file_name="
                        + metadata.getFileName()
                        + "&thread_type=waiting_thread\" target=\"_blank\">【"
                        + (int) (waiting / (threadCount / 100.0))
                        + "% 的线程正在等锁】</a>--><br>        可能是线程太拥塞，也可能是发生了死锁。如果锁对象没有一个持有它的线程，那么可能被其他资源或者系统线程持有。你可以检查下等待态的线程.</p><br></td></tr>");
        } else if (deadlocks > 0) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData.append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System " + "><p>检测到 "
                            + deadlocks + " 个死锁. 你可以到下面看到更多死锁信息<br>");
        }

        if ((threadCount > 0) && ((sleeping / (threadCount / 100.0)) > 25.0)) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData
                .append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System"
                        + "><p><a href=\"/thread/threads.htm?file_name="
                        + metadata.getFileName()
                        + "&thread_type=sleeping_thread\" target=\"_blank\">【"
                        + (int) (sleeping / (threadCount / 100.0))
                        + "% 的线程正处于休眠态】</a>--><br>        这可能是他们都在等一个抢手的资源，也可能是正常的，他们是需要等待某个时机才能继续做，可以通过左侧'休眠态线程'看到更多的信息.</p><br></td></tr>");

        }

        if (monitorsWithoutLocksCount > 0) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData
                .append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System"
                        + "><p><a href=\"/thread/monitors.htm?file_name="
                        + metadata.getFileName()
                        + "&monitor_type=without_locked_monitors\" target=\"_blank\" >【存在没有被任何线程持有的监控对象】</a>--><br>这些锁可能被某个系统线程或者额外的资源持有，可通过左侧'未被持有的锁对象'看到更多信息.</p><br></td></tr>");
        }

        if ((threadCount > 0) && (overallThreadsWaitingWithoutLocks / (threadCount / 100.0) > 50.0)) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData
                .append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System "
                        + "<p>【"
                        + (int) (overallThreadsWaitingWithoutLocks / (threadCount / 100.0))
                        + "% 的线程正在等待一个没有被任何线程持有的监控对象】--><br>        很有可能是在进行垃圾回收，并阻塞了所有的监控对象'</p></td></tr>");
        }
        metadata.setAnalyzerReport(statData.toString());
    }
}
