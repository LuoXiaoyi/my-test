package com.jvm.mem.oom;

/**
 * test
 * Out of memory: Kill process or sacrifice child
 *
 * @author xiluo
 * @createTime 2019-10-31 21:51
 **/
public class KillProcessOrSacrificeChild {
    /**
     * -Xmx2g
     * swap configuration:
     * swapoff -a
     * dd if=/dev/zero of=swapfile bs=1024 count=655360
     * mkswap swapfile
     * swapon swapfile
     *
     * result:
     * Jun 4 07:41:59 plumbr kernel: [70667120.897649] Out of memory: Kill
     * process 29957 (java) score 366 or sacrifice child
     * Jun 4 07:41:59 plumbr kernel: [70667120.897701] Killed process
     * 29957 (java) total-vm:2532680kB, anon-rss:1416508kB, file-rss:0kB
     *
     * @param args
     */
    public static void main(String[] args) {
        java.util.List<int[]> l = new java.util.ArrayList();
        for (int i = 10000; i < 100000; i++) {
            try {
                l.add(new int[100_000_000]);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
