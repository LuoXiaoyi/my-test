package com.perfma.thread;

import com.perfma.thread.model.MonitorInfoModel;
import com.perfma.thread.model.StackInfoModel;
import com.perfma.thread.model.ThreadDumpMetadata;
import com.perfma.thread.model.ThreadInfoModel;
import com.perfma.thread.util.DateMatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public class HotspotThreadDumpParser extends AbstractThreadDumpParser {

    @SuppressWarnings("rawtypes")
    public HotspotThreadDumpParser(File file, BufferedReader bis, Map threadStore, int lineCounter,
                                   int startCounter, DateMatcher dm) {
        super(file, bis, dm);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ThreadDumpMetadata parse() {
        boolean retry = false;
        String line = null;
        do {
            ThreadDumpMetadata metadata = new ThreadDumpMetadata();
            metadata.setFileName(this.getFile().getName());
            try {
                String title = null;
                StringBuffer content = null;
                boolean inLocking = false;
                boolean inSleeping = false;
                boolean inBlocking = false;
                boolean inRunnable = false;
                boolean locked = true;
                boolean finished = false;
                StringBuffer sb = new StringBuffer();

                MonitorMap mmap = new MonitorMap();
                Stack monitorStack = new Stack();
                int singleLineCounter = 0;
                boolean concurrentSyncsFlag = false;
                Matcher matched = getDm().getLastMatch();
                ThreadInfoModel parent = new ThreadInfoModel();
                int lineNum = 0;
                while (getBis().ready() && !finished) {
                    line = getNextLine();
                    lineNum++;
                    if (line != null && !line.trim().equals("")) {
                        if (locked) {
                            if (line.indexOf("Full thread dump") >= 0) {
                                locked = false;
                                if (matched != null && matched.matches()) {
                                    String parsedStartTime = matched.group(1);
                                    metadata.setDumpTime(parsedStartTime);
                                    parsedStartTime = null;
                                    matched = null;
                                    getDm().resetLastMatch();
                                }
                            } else if (!getDm().isPatternError()
                                       && (getDm().getRegexPattern() != null)) {
                                Matcher m = getDm().checkForDateMatch(line);
                                if (m != null) {
                                    matched = m;
                                }
                            }
                        } else {
                            if (line.startsWith("\"")) {
                                concurrentSyncsFlag = false;
                                if (title != null) {
                                    String token[] = getThreadTokens(title);
                                    parent.setContent(content.toString());
                                    parent.setStackLines(singleLineCounter);
                                    parent.setName(token[0]);
                                    parent.setType(token[1]);
                                    parent.setPrio(token[2]);
                                    parent.setThreadId(token[3]);
                                    parent.setNativeId(token[4]);
                                    parent.setState(token[5]);
                                    parent.setAddressRange(token[6]);
                                    parent.setTitle("【" + token[0] + "】【" + token[1] + "】【prio="
                                                    + token[2] + "】【tid=" + token[3] + "】【nid="
                                                    + token[4] + "】【" + token[5] + "】【stack_depth="
                                                    + parent.getStackLines() + "】");
                                    metadata.addThread(parent);
                                    // 设置为最大线程栈的线程
                                    if (metadata.getMaxStackDepth() < singleLineCounter) {
                                        metadata.setMaxStackDepth(singleLineCounter);
                                        metadata.setMaxStackDepthThread(parent);
                                    }
                                }
                                if (inBlocking) {
                                    inBlocking = false;
                                    metadata.addBlockingThread(parent);
                                }
                                if (inSleeping) {
                                    inSleeping = false;
                                    metadata.addSleepingThread(parent);
                                }
                                if (inRunnable) {
                                    inRunnable = false;
                                    metadata.addRunnableThread(parent);
                                }
                                if (inLocking) {
                                    inLocking = false;
                                    metadata.addLockingThread(parent);
                                }
                                singleLineCounter = 0;
                                while (!monitorStack.empty()) {
                                    mmap.parseAndAddThread((String) monitorStack.pop(), title,
                                        parent);
                                }

                                title = line;
                                if (line.indexOf("runnable") != -1) {
                                    inRunnable = true;
                                }
                                content = new StringBuffer();
                                content.append(line);
                                content.append("\n");
                                parent = new ThreadInfoModel();
                            } else {
                                StackInfoModel t = metadata.getStackInfo(line, parent, inRunnable);
                                parent.addChild(t);
                                int index = 0;
                                if ((index = line.indexOf("at ")) >= 0) {
                                    singleLineCounter++;
                                    content.append(line);
                                    content.append("\n");
                                    String clazzmethodline = line.substring(index + 3,
                                        line.length());
                                    t.setTitle("【at】" + "【" + clazzmethodline + "】");
                                    t.setClazzmethodline(clazzmethodline);
                                } else if (line.indexOf("java.lang.Thread.State") >= 0) {
                                    content.append(line);
                                    content.append("\n");
                                    if (title.indexOf("t@") > 0) {
                                        String state = line.substring(line.indexOf(':') + 1).trim();
                                        if (state.indexOf(' ') > 0) {
                                            title += " state="
                                                     + state.substring(0, state.indexOf(' '));
                                        } else {
                                            title += " state=" + state;
                                        }
                                    }
                                } else {
                                    if (line.indexOf("Locked ownable synchronizers:") >= 0) {
                                        concurrentSyncsFlag = true;
                                        line = linkifyMonitor(line);
                                        content.append(line);
                                        content.append("\n");
                                    } else if (line.indexOf("- waiting on") >= 0) {
                                        monitorStack.push(line);
                                        line = linkifyMonitor(line);
                                        content.append(line);
                                        inSleeping = true;
                                        content.append("\n");
                                    } else if (line.indexOf("- parking to wait") >= 0) {
                                        monitorStack.push(line);
                                        line = linkifyMonitor(line);
                                        content.append(line);
                                        inSleeping = true;
                                        content.append("\n");
                                    } else if (line.indexOf("- waiting to") >= 0) {
                                        monitorStack.push(line);
                                        line = linkifyMonitor(line);
                                        content.append(line);
                                        inBlocking = true;
                                        content.append("\n");
                                    } else if (line.indexOf("- locked") >= 0) {
                                        monitorStack.push(line);
                                        line = linkifyMonitor(line);
                                        content.append(line);
                                        inLocking = true;
                                        content.append("\n");
                                    } else if (line.indexOf("- ") >= 0) {
                                        if (concurrentSyncsFlag) {
                                            content.append(line);
                                            monitorStack.push(line);
                                        } else {
                                            content.append(line);
                                        }
                                        content.append("\n");
                                    }
                                    t.setTitle(line);
                                }
                            }

                            if ((line.indexOf("\"Suspend Checker Thread\"") >= 0)
                                || (line.indexOf("\"VM Periodic Task Thread\"") >= 0)
                                || (line.indexOf("<EndOfDump>") >= 0)) {
                                finished = true;
                                getBis().mark(getMarkSize());
                                if ((checkForDeadlocks(metadata)) == 0) {
                                    getBis().reset();
                                }
                                if (!checkThreadDumpStatData(metadata)) {
                                    getBis().reset();
                                }
                                getBis().mark(getMarkSize());
                                getBis().reset();
                            }

                        }
                    }
                    sb.append(line).append("<br>");
                }

                if (title != null) {
                    String token[] = getThreadTokens(title);
                    parent.setTitle(title);
                    parent.setContent(content.toString());
                    parent.setStackLines(singleLineCounter);
                    parent.setName(token[0]);
                    parent.setType(token[1]);
                    parent.setPrio(token[2]);
                    parent.setThreadId(token[3]);
                    parent.setNativeId(token[4]);
                    parent.setState(token[5]);
                    parent.setAddressRange(token[6]);
                    metadata.addThread(parent);
                    if (line.indexOf("runnable") != -1) {
                        inRunnable = true;
                    }
                    // 设置为最大线程栈的线程
                    if (metadata.getMaxStackDepth() < singleLineCounter) {
                        metadata.setMaxStackDepth(singleLineCounter);
                        metadata.setMaxStackDepthThread(parent);
                    }
                }
                if (inBlocking) {
                    inBlocking = false;
                    metadata.addBlockingThread(parent);
                }
                if (inSleeping) {
                    inSleeping = false;
                    metadata.addSleepingThread(parent);
                }
                if (inLocking) {
                    inLocking = false;
                    metadata.addLockingThread(parent);
                }
                if (inRunnable) {
                    inRunnable = false;
                    metadata.addRunnableThread(parent);
                }
                singleLineCounter = 0;
                while (!monitorStack.empty()) {
                    mmap.parseAndAddThread((String) monitorStack.pop(), title, parent);
                }

                if (mmap.size() > 0) {
                    dumpMonitors(mmap, metadata);
                    dumpBlockingMonitors(mmap, metadata);
                }
                metadata.setFileLine(lineNum);
                metadata.setFileContent(sb.toString());
                // //每个栈按照被持有的线程数进行排序
                // metadata.sortStackInfoList();
                ThreadDumpReportAnalyzer.analyzeDump(metadata);
                return metadata;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                retry = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (retry);
        return null;
    }

    private String linkifyMonitor(String line) {
        return line;
    }

    private String linkifyDeadlockInfo(String line) {
        if (line != null && line.indexOf("Ox") >= 0) {
            String begin = line.substring(0, line.indexOf("0x"));
            int objectBegin = line.lastIndexOf("0x");
            int monitorBegin = line.indexOf("0x");
            String monitorHex = line.substring(monitorBegin, monitorBegin + 10);

            String monitor = line.substring(objectBegin, objectBegin + 10);
            String end = line.substring(line.indexOf("0x") + 10);

            monitor = "<a href=\"monitor://<" + monitor + ">\">" + monitorHex + "</a>";
            return (begin + monitor + end);
        } else {
            return (line);
        }
    }

    private boolean checkThreadDumpStatData(ThreadDumpMetadata metadata) throws IOException {
        boolean finished = false;
        boolean found = false;
        StringBuffer hContent = new StringBuffer();
        int heapLineCounter = 0;
        while (getBis().ready() && !finished) {
            String line = getNextLine();
            if (!found && !line.equals("")) {
                if (line.trim().startsWith("Heap")) {
                    found = true;
                }
            } else if (found) {
                if (heapLineCounter < 7) {
                    hContent.append(line).append("\n");
                } else {
                    finished = true;
                }
                heapLineCounter++;
            }
        }
        if (hContent.length() > 0) {
            metadata.setHeapInfo(hContent.toString());
        }

        return (found);
    }

    private int checkForDeadlocks(ThreadDumpMetadata metadata) throws IOException {
        boolean finished = false;
        boolean found = false;
        int deadlocks = 0;
        StringBuffer dContent = new StringBuffer();
        boolean first = true;

        while (getBis().ready() && !finished) {
            String line = getNextLine();

            if (!found && !line.equals("")) {
                if (line.trim().startsWith("Found one Java-level deadlock")) {
                    found = true;
                    dContent.append("<body bgcolor=\"ffffff\"><font size=-1").append("><b>");
                    dContent.append("发现一个java级别的死锁");
                    dContent.append("</b><hr></font><pre>\n");
                }
            } else if (found) {
                if (line.startsWith("Found one Java-level deadlock")) {
                    if (dContent.length() > 0) {
                        deadlocks++;
                    }
                    dContent = new StringBuffer();
                    dContent.append("</pre><b><font size=-1").append(">");
                    dContent.append("发现一个java级别的死锁");
                    dContent.append("</b><hr></font><pre>\n");
                    first = true;
                } else if ((line.indexOf("Found") >= 0)
                           && (line.endsWith("deadlocks.") || line.endsWith("deadlock."))) {
                    finished = true;
                } else if (line.startsWith("=======")) {
                    // ignore this line
                } else if (line.indexOf(" monitor 0x") >= 0) {
                    dContent.append(linkifyDeadlockInfo(line));
                    dContent.append("\n");
                } else if (line.indexOf("Java stack information for the threads listed above") >= 0) {
                    dContent.append("</pre><br><font size=-1").append("><b>");
                    dContent.append("Java stack information for the threads listed above");
                    dContent.append("</b><hr></font><pre>");
                    first = true;
                } else if ((line.indexOf("- waiting on") >= 0)
                           || (line.indexOf("- waiting to") >= 0)
                           || (line.indexOf("- locked") >= 0)
                           || (line.indexOf("- parking to wait") >= 0)) {

                    dContent.append(linkifyMonitor(line));
                    dContent.append("\n");

                } else if (line.trim().startsWith("\"")) {
                    dContent.append("</pre>");
                    if (first) {
                        first = false;
                    } else {
                        dContent.append("<br>");
                    }
                    dContent.append("<b><font size=-1").append("><code>");
                    dContent.append(line);
                    dContent.append("</font></code></b><pre>");
                } else {
                    dContent.append(line);
                    dContent.append("\n");
                }
            }
        }
        if (dContent.length() > 0) {
            deadlocks++;
            metadata.setDeadLockInfo(dContent.toString());
            metadata.setDeadLockNum(deadlocks);
        }

        return deadlocks;
    }

    @SuppressWarnings("rawtypes")
    private void dumpMonitors(MonitorMap mmap, ThreadDumpMetadata metadata) {
        Iterator iter = mmap.iterOfKeys();
        int overallThreadsWaiting = 0;
        while (iter.hasNext()) {
            String monitor = (String) iter.next();

            MonitorInfoModel monitorModel = new MonitorInfoModel(monitor);

            Map[] threads = mmap.getFromMonitorMap(monitor);
            int locks = 0;
            // ThreadInfoModel lockandsleeps = null;
            // ThreadInfoModel lockandwaits = null;
            // ThreadInfoModel locked = null;
            Iterator iterLocks = threads[MonitorMap.LOCK_THREAD_POS].keySet().iterator();
            while (iterLocks.hasNext()) {
                String thread = (String) iterLocks.next();
                ThreadInfoModel threadInfo = (ThreadInfoModel) threads[MonitorMap.LOCK_THREAD_POS]
                    .get(thread);
                if (threads[MonitorMap.SLEEP_THREAD_POS].containsKey(thread)) {
                    // if (lockandsleeps == null) {
                    // lockandsleeps = new
                    // ThreadInfoModel("locks and sleeps on monitor");
                    // }
                    // lockandsleeps.addChild(threadInfo);
                    monitorModel.addLockAndSleepOnMonitor(threadInfo);
                } else if (threads[MonitorMap.WAIT_THREAD_POS].containsKey(thread)) {
                    // if (lockandwaits == null) {
                    // lockandwaits = new
                    // ThreadInfoModel("locks and waits on monitor");
                    // }
                    // lockandwaits.addChild(threadInfo);
                    monitorModel.addLockAndWaitOnMonitor(threadInfo);
                } else {
                    // if (locked == null) {
                    // locked = new ThreadInfoModel("locked");
                    // }
                    // locked.addChild(threadInfo);
                    monitorModel.addLocked(threadInfo);
                }
                locks++;
            }
            int waits = 0;
            // ThreadInfoModel waitsonmonitor = null;
            Iterator iterWaits = threads[MonitorMap.WAIT_THREAD_POS].keySet().iterator();
            while (iterWaits.hasNext()) {
                String thread = (String) iterWaits.next();
                ThreadInfoModel threadInfo = (ThreadInfoModel) threads[MonitorMap.WAIT_THREAD_POS]
                    .get(thread);
                if (!threads[MonitorMap.LOCK_THREAD_POS].containsKey(thread)) {
                    // if (waitsonmonitor == null) {
                    // waitsonmonitor = new ThreadInfoModel("waits on monitor");
                    // }
                    // waitsonmonitor.addChild(threadInfo);
                    monitorModel.addWaitOnMonitor(threadInfo);
                    waits++;
                }
            }
            if (locks == 0) {
                overallThreadsWaiting += waits;
                metadata.addMonitorWithoutLocks(monitorModel);
            } else {
                metadata.addMonitorWithLocks(monitorModel);
            }
        }
        metadata.setOverallThreadsWaitingWithoutLocksCount(overallThreadsWaiting);
    }

    @SuppressWarnings("rawtypes")
    private void dumpBlockingMonitors(MonitorMap mmap, ThreadDumpMetadata metadata) {
        Map directChildMap = new HashMap();
        fillBlockingThreadMaps(mmap, directChildMap, metadata);
        // renormalizeBlockingThreadTree(mmap, directChildMap);
        // for (Iterator iter = directChildMap.entrySet().iterator();
        // iter.hasNext();) {
        // DefaultMutableTreeNode threadNode = (DefaultMutableTreeNode)
        // ((Map.Entry) iter.next())
        // .getValue();
        //
        // updateChildCount(threadNode, true);
        // }
        directChildMap.clear();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void fillBlockingThreadMaps(MonitorMap mmap, Map directChildMap,
                                        ThreadDumpMetadata metadata) {
        ThreadInfoModel otherThreadInfoModel = new ThreadInfoModel();
        otherThreadInfoModel.setDumy(true);
        for (Iterator<String> iter = mmap.iterOfKeys(); iter.hasNext();) {
            String monitor = (String) iter.next();
            Map<String, ThreadInfoModel>[] threads = mmap.getFromMonitorMap(monitor);
            ThreadInfoModel threadInfoModel = getLockingThread(threads);
            if (threadInfoModel == null) {
                threadInfoModel = otherThreadInfoModel;
                threadInfoModel.setTitle("dumy thread");
            }

            MonitorInfoModel monitorModel = metadata.getMonitor(monitor);
            threadInfoModel.addOwnedMonitor(monitorModel);

            for (Iterator<String> iterWaits = threads[MonitorMap.WAIT_THREAD_POS].keySet()
                .iterator(); iterWaits.hasNext();) {
                String thread = (String) iterWaits.next();
                if (!threads[MonitorMap.LOCK_THREAD_POS].containsKey(thread)) {
                    monitorModel
                        .addBlockedThread((ThreadInfoModel) threads[MonitorMap.WAIT_THREAD_POS]
                            .get(thread));
                }
            }
            if (monitorModel.hasBlockedThread()) {
                metadata.addThreadBlockSome(threadInfoModel);
                directChildMap.put(monitor, threadInfoModel);

                Iterator<MonitorInfoModel> it = threadInfoModel.getOwnedMonitor().iterator();
                Set<ThreadInfoModel> blockedThread = new HashSet<ThreadInfoModel>();
                while (it.hasNext()) {
                    MonitorInfoModel mm = it.next();
                    blockedThread.addAll(mm.getBlockedThread());
                }
                threadInfoModel.setBlockedThreadNum(blockedThread.size());
                // 设置为最大线程栈的线程
                if (!threadInfoModel.getTitle().equals("dumy thread")
                    && metadata.getMaxBlockNum() < blockedThread.size()) {
                    metadata.setMaxBlockNum(blockedThread.size());
                    metadata.setMaxBlockNumThread(threadInfoModel);
                    metadata.setMaxBlockNumMonitor(monitorModel);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private ThreadInfoModel getLockingThread(Map[] threads) {
        int lockingThreadCount = threads[MonitorMap.LOCK_THREAD_POS].keySet().size();
        if (lockingThreadCount == 1) {
            return (ThreadInfoModel) threads[MonitorMap.LOCK_THREAD_POS].values().iterator().next();
        }

        for (Iterator iterLocks = threads[MonitorMap.LOCK_THREAD_POS].keySet().iterator(); iterLocks
            .hasNext();) {
            String thread = (String) iterLocks.next();
            if (!threads[MonitorMap.SLEEP_THREAD_POS].containsKey(thread)) {
                return (ThreadInfoModel) threads[MonitorMap.LOCK_THREAD_POS].get(thread);
            }
        }

        return null;
    }

    public String[] getThreadTokens(String name) {
        String[] tokens = null;

        if (name.indexOf("prio") > 0) {
            tokens = new String[7];

            tokens[0] = name.substring(1, name.lastIndexOf('"'));
            tokens[1] = name.indexOf("daemon") > 0 ? "Daemon" : "Task";

            String strippedToken = name.substring(name.lastIndexOf('"') + 1);

            if (strippedToken.indexOf("tid=") >= 0) {
                tokens[2] = strippedToken.substring(strippedToken.indexOf("prio=") + 5,
                    strippedToken.indexOf("tid=") - 1);
            } else {
                tokens[2] = strippedToken.substring(strippedToken.indexOf("prio=") + 5);
            }

            if ((strippedToken.indexOf("tid=") >= 0) && (strippedToken.indexOf("nid=") >= 0)) {
                tokens[3] = String.valueOf(Long.parseLong(
                    strippedToken.substring(strippedToken.indexOf("tid=") + 6,
                        strippedToken.indexOf("nid=") - 1), 16));
            } else if (strippedToken.indexOf("tid=") >= 0) {
                tokens[3] = String.valueOf(Long.parseLong(
                    strippedToken.substring(strippedToken.indexOf("tid=") + 6), 16));
            }

            // default for token 6 is:
            tokens[6] = "<no address range>";

            if ((strippedToken.indexOf("nid=") >= 0)
                && (strippedToken.indexOf(" ", strippedToken.indexOf("nid="))) >= 0) {
                if (strippedToken.indexOf("nid=0x") > 0) { // is hexadecimal
                    String nidToken = strippedToken.substring(strippedToken.indexOf("nid=") + 6,
                        strippedToken.indexOf(" ", strippedToken.indexOf("nid=")));
                    tokens[4] = String.valueOf(Long.parseLong(nidToken, 16));
                } else { // is decimal
                    String nidToken = strippedToken.substring(strippedToken.indexOf("nid=") + 4,
                        strippedToken.indexOf(" ", strippedToken.indexOf("nid=")));
                    tokens[4] = nidToken;
                }

                if (strippedToken.indexOf('[') > 0) {
                    if (strippedToken.indexOf("lwp_id=") > 0) {
                        tokens[5] = strippedToken.substring(
                            strippedToken.indexOf(" ", strippedToken.indexOf("lwp_id=")) + 1,
                            strippedToken.indexOf('[', strippedToken.indexOf("lwp_id=")) - 1);
                    } else {
                        tokens[5] = strippedToken.substring(
                            strippedToken.indexOf(" ", strippedToken.indexOf("nid=")) + 1,
                            strippedToken.indexOf('[', strippedToken.indexOf("nid=")) - 1);
                    }
                    tokens[6] = strippedToken.substring(strippedToken.indexOf('['));
                } else {
                    tokens[5] = strippedToken.substring(strippedToken.indexOf(" ",
                        strippedToken.indexOf("nid=")) + 1);
                }
            } else if (strippedToken.indexOf("nid=") >= 0) {
                String nidToken = strippedToken.substring(strippedToken.indexOf("nid=") + 6);
                // nid is at the end.
                if (nidToken.indexOf("0x") > 0) { // is hexadecimal
                    tokens[4] = String.valueOf(Long.parseLong(nidToken, 16));
                } else {
                    tokens[4] = nidToken;
                }
            }
        } else {
            tokens = new String[3];
            tokens[0] = name.substring(1, name.lastIndexOf('"'));
            if (name.indexOf("nid=") > 0) {
                tokens[1] = name.substring(name.indexOf("nid=") + 4, name.indexOf("state=") - 1);
                tokens[2] = name.substring(name.indexOf("state=") + 6);
            } else if (name.indexOf("t@") > 0) {
                tokens[1] = name.substring(name.indexOf("t@") + 2, name.indexOf("state=") - 1);
                tokens[2] = name.substring(name.indexOf("state=") + 6);
            } else {
                tokens[1] = name.substring(name.indexOf("id=") + 3, name.indexOf(" in"));
                tokens[2] = name.substring(name.indexOf(" in") + 3);
            }
        }

        return (tokens);
    }

    public static boolean checkForSupportedThreadDump(String logLine) {
        return (logLine.trim().indexOf("Full thread dump") >= 0);
    }

    protected String getNextLine() throws IOException {
        return getBis().readLine();
    }
}
