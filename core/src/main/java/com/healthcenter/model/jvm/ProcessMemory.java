package com.healthcenter.model.jvm;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiluo
 * @date 2020/3/27 10:18
 * @Version 1.0
 **/
public class ProcessMemory implements Serializable {
    private static final long serialVersionUID = 1246920979464282928L;

    /**
     * VSS - Virtual Set Size （用处不大）
     * 和 top 的 VIRT 一个意思，虚拟耗用内存（包含共享库占用的全部内存，以及分配但未使用内存）。
     * 其大小还包括了可能不在RAM中的内存（比如虽然malloc分配了空间，但尚未写入）。VSS 很少被用于判断一个进程的真实内存使用量。
     */
    private long vss;

    /**
     * RSS - Resident Set Size （用处不大）
     * 和 top 的 RES 一个意思，实际使用物理内存（包含共享库占用的全部内存）。但是RSS还是可能会造成误导，因为它仅仅表示该进程所使用的所有共享库的大小，
     * 它不管有多少个进程使用该共享库，该共享库仅被加载到内存一次。所以RSS并不能准确反映单进程的内存占用情况</p>
     */
    private long rss;

    /**
     * PSS - Proportional Set Size （仅供参考）
     * 和 top 的 SHR 差不多一个意思，实际使用的物理内存（比例分配共享库占用的内存，按照进程数等比例划分）。
     * 例如：如果有三个进程都使用了一个共享库，共占用了30页内存。那么PSS将认为每个进程分别占用该共享库10页的大小。
     * PSS是非常有用的数据，因为系统中所有进程的PSS都相加的话，就刚好反映了系统中的总共占用的内存。
     * 而当一个进程被销毁之后，其占用的共享库那部分比例的PSS，将会再次按比例分配给余下使用该库的进程。
     * 这样PSS可能会造成一点的误导，因为当一个进程被销毁后，PSS不能准确地表示返回给全局系统的内存。</p>
     */
    private long pss;

    /**
     * <p>USS - Unique Set Size （非常有用）<br>
     * 进程独自占用的物理内存（不包含共享库占用的内存）。
     * USS是非常非常有用的数据，因为它反映了运行一个特定进程真实的边际成本（增量成本）。
     * 当一个进程被销毁后，USS是真实返回给系统的内存。当进程中存在一个可疑的内存泄露时，USS是最佳观察数据。</p>
     */
    private long uss;

    private MemoryUsage heapUsage;
    private MemoryUsage nonHeapUsage;
    private List<MemoryPool> memoryPools;

    public void addMemoryPools(MemoryPool... pools) {
        if (pools != null && pools.length > 0) {
            if (memoryPools == null) {
                memoryPools = new LinkedList<>();
            }

            memoryPools.addAll(Arrays.asList(pools));
        }
    }

    public long getVss() {
        return vss;
    }

    public void setVss(long vss) {
        this.vss = vss;
    }

    public long getRss() {
        return rss;
    }

    public void setRss(long rss) {
        this.rss = rss;
    }

    public long getPss() {
        return pss;
    }

    public void setPss(long pss) {
        this.pss = pss;
    }

    public long getUss() {
        return uss;
    }

    public void setUss(long uss) {
        this.uss = uss;
    }

    public MemoryUsage getHeapUsage() {
        return heapUsage;
    }

    public void setHeapUsage(MemoryUsage heapUsage) {
        this.heapUsage = heapUsage;
    }

    public MemoryUsage getNonHeapUsage() {
        return nonHeapUsage;
    }

    public void setNonHeapUsage(MemoryUsage nonHeapUsage) {
        this.nonHeapUsage = nonHeapUsage;
    }

    public List<MemoryPool> getMemoryPools() {
        return memoryPools;
    }

    public void setMemoryPools(List<MemoryPool> memoryPools) {
        this.memoryPools = memoryPools;
    }
}
