package com.perfma.thread;

import com.perfma.thread.model.ThreadDumpMetadata;
import com.perfma.thread.util.DateMatcher;

import javax.swing.tree.TreePath;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public abstract class AbstractThreadDumpParser implements ThreadDumpParser {

    private BufferedReader bis = null;
    private int markSize = 6319200;
    private DateMatcher dm = null;

    private File file;

    protected AbstractThreadDumpParser(File file, BufferedReader bis, DateMatcher dm) {
        setBis(bis);
        setDm(dm);
        setFile(file);
    }

    protected String getDumpStringFromTreePath(TreePath path) {
        String[] elems = path.toString().split(",");
        if (elems.length > 1) {
            return (elems[elems.length - 1].substring(0, elems[elems.length - 1].lastIndexOf(']')).trim());
        } else {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public void findLongRunningThreads(Map dumpStore, TreePath[] paths, int minOccurence, String regex) {
        diffDumps("Long running thread detection", dumpStore, paths, minOccurence, regex);
    }

    @SuppressWarnings("rawtypes")
    public void mergeDumps(Map dumpStore, TreePath[] dumps, int minOccurence, String regex) {
        diffDumps("Merge", dumpStore, dumps, minOccurence, regex);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void diffDumps(String prefix, Map dumpStore, TreePath[] dumps, int minOccurence, String regex) {
        Vector keys = new Vector(dumps.length);

        for (int i = 0; i < dumps.length; i++) {
            String dumpName = getDumpStringFromTreePath(dumps[i]);
            if (dumpName.indexOf(" at") > 0) {
                dumpName = dumpName.substring(0, dumpName.indexOf(" at"));
            } else if (dumpName.indexOf(" around") > 0) {
                dumpName = dumpName.substring(0, dumpName.indexOf(" around"));
            }
            keys.add(dumpName);
        }

        if (dumpStore.get(keys.get(0)) != null) {
            Iterator dumpIter = ((Map) dumpStore.get(keys.get(0))).keySet().iterator();

            while (dumpIter.hasNext()) {
                String threadKey = ((String) dumpIter.next()).trim();
                int occurence = 0;

                if (regex == null || regex.equals("") || threadKey.matches(regex)) {
                    for (int i = 1; i < dumps.length; i++) {
                        Map threads = (Map) dumpStore.get(keys.get(i));
                        if (threads.containsKey(threadKey)) {
                            occurence++;
                        }
                    }

                    if (occurence >= (minOccurence - 1)) {
                        StringBuffer content = new StringBuffer("<body bgcolor=\"ffffff\"><b><font size=").append("-1").append(">").append((String)
                                keys.get(0)).append("</b></font><hr><pre><font size=").append("-1").append(">").append(fixMonitorLinks((String) (
                                        (Map) dumpStore.get(keys.get(0))).get(threadKey), (String) keys.get(0)));

                        int maxLines = 0;
                        for (int i = 1; i < dumps.length; i++) {
                            if (((Map) dumpStore.get(keys.get(i))).containsKey(threadKey)) {
                                content.append("\n\n</pre><b><font size=");
                                content.append("-1");
                                content.append(">");
                                content.append(keys.get(i));
                                content.append("</font></b><hr><pre><font size=");
                                content.append("-1");
                                content.append(">");
                                content.append(fixMonitorLinks((String) ((Map) dumpStore.get(keys.get(i))).get(threadKey), (String) keys.get(i)));
                                int countLines = countLines(((String) ((Map) dumpStore.get(keys.get(i))).get(threadKey)));
                                maxLines = maxLines > countLines ? maxLines : countLines;
                            }
                        }
                    }
                }
            }
        }
    }

    private int countLines(String input) {
        int pos = 0;
        int count = 0;
        while (input.indexOf('\n', pos) > 0) {
            count++;
            pos = input.indexOf('\n', pos) + 1;
        }

        return (count);
    }

    @SuppressWarnings({"unused", "rawtypes"})
    private String getStatInfo(Vector keys, String prefix, int minOccurence, int threadCount) {
        StringBuffer statData = new StringBuffer("<body bgcolor=\"#ffffff\"><font face=System><b><font face=System> ");

        statData.append("<b>" + prefix + "</b><hr><p><i>");
        for (int i = 0; i < keys.size(); i++) {
            statData.append(keys.get(i));
            if (i < keys.size() - 1) {
                statData.append(", ");
            }
        }
        statData.append("</i></p><br>" + "<table border=0><tr bgcolor=\"#dddddd\"><td><font face=System " + ">Overall Thread Count</td><td " +
                "width=\"150\"></td><td><b><font face=System>");
        statData.append(threadCount);
        statData.append("</b></td></tr>");
        statData.append("<tr bgcolor=\"#eeeeee\"><td><font face=System " + ">Minimum Occurence of threads</td><td width=\"150\"></td><td><b><font " +
                "face=System>");
        statData.append(minOccurence);
        statData.append("</b></td></tr>");

        if (threadCount == 0) {
            statData.append("<tr bgcolor=\"#ffffff\"<td></td></tr>");
            statData.append("<tr bgcolor=\"#cccccc\"><td colspan=2><font face=System " + "><p>No threads were found which occured at least " +
                    minOccurence + " times.<br>" + "You should check your dumps for long running threads " + "or adjust the minimum occurence.</p>");
        }

        statData.append("</table>");

        return statData.toString();
    }

    private String fixMonitorLinks(String fixString, String dumpName) {
        if (fixString.indexOf("monitor://") > 0) {
            fixString = fixString.replaceAll("monitor://", "monitor:/" + dumpName + "/");
        }
        return (fixString);
    }

    protected BufferedReader getBis() {
        return bis;
    }

    protected abstract String[] getThreadTokens(String title);

    protected void setBis(BufferedReader bis) {
        this.bis = bis;
    }

    public ThreadDumpMetadata parse() {
        return null;
    }

    public void close() throws IOException {
        if (getBis() != null) {
            getBis().close();
        }
    }

    protected int getMarkSize() {
        return markSize;
    }

    protected void setMarkSize(int markSize) {
        this.markSize = markSize;
    }

    public DateMatcher getDm() {
        return dm;
    }

    public void setDm(DateMatcher dm) {
        this.dm = dm;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
