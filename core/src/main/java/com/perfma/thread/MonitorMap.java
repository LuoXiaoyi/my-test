package com.perfma.thread;

import com.perfma.thread.model.ThreadInfoModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MonitorMap {

    public static final int LOCK_THREAD_POS  = 0;

    public static final int WAIT_THREAD_POS  = 1;

    public static final int SLEEP_THREAD_POS = 2;

    @SuppressWarnings("rawtypes")
    private Map             monitorMap       = null;

    public MonitorMap() {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addToMonitorMap(String key, Map[] objectSet) {
        if (monitorMap == null) {
            monitorMap = new HashMap();
        }
        monitorMap.put(key, objectSet);
    }

    public boolean hasInMonitorMap(String key) {
        return (monitorMap != null && monitorMap.containsKey(key));
    }

    @SuppressWarnings("rawtypes")
    public Map[] getFromMonitorMap(String key) {
        return (monitorMap != null && hasInMonitorMap(key) ? (Map[]) monitorMap.get(key) : null);
    }

    public void addWaitToMonitor(String key, String waitThread, ThreadInfoModel threadContent) {
        addToMonitorValue(key, WAIT_THREAD_POS, waitThread, threadContent);
    }

    public void addLockToMonitor(String key, String lockThread, ThreadInfoModel threadContent) {
        addToMonitorValue(key, LOCK_THREAD_POS, lockThread, threadContent);
    }

    public void addSleepToMonitor(String key, String sleepThread, ThreadInfoModel threadContent) {
        addToMonitorValue(key, SLEEP_THREAD_POS, sleepThread, threadContent);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addToMonitorValue(String key, int pos, String threadTitle, ThreadInfoModel thread) {
        Map[] objectSet = null;

        if (hasInMonitorMap(key)) {
            objectSet = getFromMonitorMap(key);
        } else {
            objectSet = new HashMap[3];
            objectSet[0] = new HashMap();
            objectSet[1] = new HashMap();
            objectSet[2] = new HashMap();
            addToMonitorMap(key, objectSet);
        }
        objectSet[pos].put(threadTitle, thread);
    }

    public void parseAndAddThread(String line, String threadTitle, ThreadInfoModel currentThread) {
        if (line == null) {
            return;
        }
        if ((line.indexOf('<') > 0)) {
            String monitor = line.substring(line.indexOf('<'));
            if (line.trim().startsWith("- waiting to lock")
                || line.trim().startsWith("- parking to wait")) {
                addWaitToMonitor(monitor, threadTitle, currentThread);
            } else if (line.trim().startsWith("- waiting on")) {
                addSleepToMonitor(monitor, threadTitle, currentThread);
            } else {
                addLockToMonitor(monitor, threadTitle, currentThread);
            }
        } else if (line.indexOf('@') > 0) {
            String monitor = "<" + line.substring(line.indexOf('@') + 1) + "> (a "
                             + line.substring(line.lastIndexOf(' '), line.indexOf('@')) + ")";
            if (line.trim().startsWith("- waiting to lock")
                || line.trim().startsWith("- parking to wait")) {
                addWaitToMonitor(monitor, threadTitle, currentThread);
            } else if (line.trim().startsWith("- waiting on")) {
                addSleepToMonitor(monitor, threadTitle, currentThread);
            } else {
                addLockToMonitor(monitor, threadTitle, currentThread);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> iterOfKeys() {
        return (monitorMap == null ? null : monitorMap.keySet().iterator());
    }

    public int size() {
        return (monitorMap == null ? 0 : monitorMap.size());
    }

}
