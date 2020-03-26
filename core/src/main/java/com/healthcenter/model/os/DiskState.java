package com.healthcenter.model.os;

/**
 * @author xiluo
 * @ClassName
 * @description <pre>
 * /proc/diskstats (since Linux 2.5.69)
 *      This file contains disk I/O statistics for each disk device.
 *      See the Linux kernel source file Documentation/iostats.txt for further information.
 *
 *      Field  1 -- # of reads completed
 *     This is the total number of reads completed successfully.
 *
 *      Field  2 -- # of reads merged, field 6 -- # of writes merged
 *     Reads and writes which are adjacent to each other may be merged for
 *     efficiency.  Thus two 4K reads may become one 8K read before it is
 *     ultimately handed to the disk, and so it will be counted (and queued)
 *     as only one I/O.  This field lets you know how often this was done.
 *
 *      Field  3 -- # of sectors read
 *     This is the total number of sectors read successfully.
 *
 *      Field  4 -- # of milliseconds spent reading
 *     This is the total number of milliseconds spent by all reads (as
 *     measured from __make_request() to end_that_request_last()).
 *
 *      Field  5 -- # of writes completed
 *     This is the total number of writes completed successfully.
 *
 *      Field  6 -- # of writes merged
 *     See the description of field 2.
 *
 *      Field  7 -- # of sectors written
 *     This is the total number of sectors written successfully.
 *
 *      Field  8 -- # of milliseconds spent writing
 *     This is the total number of milliseconds spent by all writes (as
 *     measured from __make_request() to end_that_request_last()).
 *
 *      Field  9 -- # of I/Os currently in progress
 *     The only field that should go to zero. Incremented as requests are
 *     given to appropriate struct request_queue and decremented as they finish.
 *
 *      Field 10 -- # of milliseconds spent doing I/Os
 *     This field increases so long as field 9 is nonzero.
 *
 *      Field 11 -- weighted # of milliseconds spent doing I/Os
 *     This field is incremented at each I/O start, I/O completion, I/O
 *     merge, or read of these stats by the number of I/Os in progress
 *     (field 9) times the number of milliseconds spent doing I/O since the
 *     last update of this field.  This can provide an easy measure of both
 *     I/O completion time and the backlog that may be accumulating.
 *
 *     eg: 10857 22 792627 157753 173683 25508 29532341 6031648 0 1499214 6191622
 *
 *      https://blog.csdn.net/yetugeng/article/details/88387811
 *      1. (rd_ios)读操作的次数。
 *      2. (rd_merges)合并读操作的次数。如果两个读操作读取相邻的数据块时，可以被合并成一个，以提高效率。合并的操作通常是I/O scheduler（也叫elevator）负责的。
 *      3. (rd_sectors)读取的扇区数量。
 *      4. (rd_ticks)读操作消耗的时间（以毫秒为单位）。每个读操作从__make_request()开始计时，到end_that_request_last()为止，包括了在队列中等待的时间。
 *      5. (wr_ios)写操作的次数。
 *      6. (wr_merges)合并写操作的次数。
 *      7. (wr_sectors)写入的扇区数量。
 *      8. (wr_ticks)写操作消耗的时间（以毫秒为单位）。
 *      9. (in_flight)当前未完成的I/O数量。在I/O请求进入队列时该值加1，在I/O结束时该值减1。
 *      注意：是I/O请求进入队列时，而不是提交给硬盘设备时。
 *      10. (io_ticks)该设备用于处理I/O的自然时间(wall-clock time)。
 *      请注意io_ticks与rd_ticks(字段#4)和wr_ticks(字段#8)的区别，rd_ticks和wr_ticks是把每一个I/O所消耗的时间累加在一起，因为硬盘设备通常可以并行处理多个I/O，
 *      所以rd_ticks和wr_ticks往往会比自然时间大。而io_ticks表示该设备有I/O（即非空闲）的时间，不考虑I/O有多少，只考虑有没有。在实际计算时，
 *      字段#9(in_flight)不为零的时候io_ticks保持计时，字段#9(in_flight)为零的时候io_ticks停止计时。
 *      11. (time_in_queue)对字段#10(io_ticks)的加权值。字段#10(io_ticks)是自然时间，不考虑当前有几个I/O，
 *      而time_in_queue是用当前的I/O数量（即字段#9 in-flight）乘以自然时间。虽然该字段的名称是time_in_queue，
 *      但并不真的只是在队列中的时间，其中还包含了硬盘处理I/O的时间。iostat在计算avgqu-sz时会用到这个字段。
 * </pre>
 * @date 2020/3/26 19:28
 * @Version 1.0
 **/
public class DiskState {
    private long totalCompletedReadNbr;
    private long totalCompletedWriteNbr;

    private long readMergedNbr;
    private long writeMergedNbr;

    private long totalReadSectorsNbr;
    private long totalWriteSectorsNbr;

    private long totalReadMs;
    private long totalWriteMs;

    /**
     * should be 0 at most time.
     */
    private long ioRequestInProgress;

    /**
     * #9(in_flight)不为零的时候io_ticks保持计时，字段#9(in_flight)为零的时候io_ticks停止计时
     */
    private long ioQueueNotEmptyMs;

    /**
     * 11. io 数量 * io 在队列上的持续时间
     */
    private long ioQueueNotEmptyWeightMs;

    public long getTotalCompletedReadNbr() {
        return totalCompletedReadNbr;
    }

    public void setTotalCompletedReadNbr(long totalCompletedReadNbr) {
        this.totalCompletedReadNbr = totalCompletedReadNbr;
    }

    public long getTotalCompletedWriteNbr() {
        return totalCompletedWriteNbr;
    }

    public void setTotalCompletedWriteNbr(long totalCompletedWriteNbr) {
        this.totalCompletedWriteNbr = totalCompletedWriteNbr;
    }

    public long getReadMergedNbr() {
        return readMergedNbr;
    }

    public void setReadMergedNbr(long readMergedNbr) {
        this.readMergedNbr = readMergedNbr;
    }

    public long getWriteMergedNbr() {
        return writeMergedNbr;
    }

    public void setWriteMergedNbr(long writeMergedNbr) {
        this.writeMergedNbr = writeMergedNbr;
    }

    public long getTotalReadSectorsNbr() {
        return totalReadSectorsNbr;
    }

    public void setTotalReadSectorsNbr(long totalReadSectorsNbr) {
        this.totalReadSectorsNbr = totalReadSectorsNbr;
    }

    public long getTotalWriteSectorsNbr() {
        return totalWriteSectorsNbr;
    }

    public void setTotalWriteSectorsNbr(long totalWriteSectorsNbr) {
        this.totalWriteSectorsNbr = totalWriteSectorsNbr;
    }

    public long getTotalReadMs() {
        return totalReadMs;
    }

    public void setTotalReadMs(long totalReadMs) {
        this.totalReadMs = totalReadMs;
    }

    public long getTotalWriteMs() {
        return totalWriteMs;
    }

    public void setTotalWriteMs(long totalWriteMs) {
        this.totalWriteMs = totalWriteMs;
    }

    public long getIoRequestInProgress() {
        return ioRequestInProgress;
    }

    public void setIoRequestInProgress(long ioRequestInProgress) {
        this.ioRequestInProgress = ioRequestInProgress;
    }

    public long getIoQueueNotEmptyMs() {
        return ioQueueNotEmptyMs;
    }

    public void setIoQueueNotEmptyMs(long ioQueueNotEmptyMs) {
        this.ioQueueNotEmptyMs = ioQueueNotEmptyMs;
    }

    public long getIoQueueNotEmptyWeightMs() {
        return ioQueueNotEmptyWeightMs;
    }

    public void setIoQueueNotEmptyWeightMs(long ioQueueNotEmptyWeightMs) {
        this.ioQueueNotEmptyWeightMs = ioQueueNotEmptyWeightMs;
    }
}
