package com.healthcenter.model.os;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiluo
 * <pre>
 * /proc/[pid]/io (since kernel 2.6.20)
 *               This file contains I/O statistics for the process, for example:
 *
 *                   # cat /proc/3828/io
 *                   rchar: 323934931
 *                   wchar: 323929600
 *                   syscr: 632687
 *                   syscw: 632675
 *                   read_bytes: 0
 *                   write_bytes: 323932160
 *                   cancelled_write_bytes: 0
 *
 *               The fields are as follows:
 *
 *               rchar: characters read
 *                      The number of bytes which this task has caused to be read from
 *                      storage.  This is simply the sum of bytes which  this  process
 *                      passed  to  read(2)  and  similar  system  calls.  It includes
 *                      things such as terminal I/O and is unaffected  by  whether  or
 *                      not actual physical disk I/O was required (the read might have
 *                      been satisfied from pagecache).
 *
 *               wchar: characters written
 *                      The number of bytes which this task has caused, or shall cause
 *                      to  be  written  to  disk.  Similar caveats apply here as with
 *                      rchar.
 *
 *               syscr: read syscalls
 *                      Attempt to count the number of read  I/O  operations—that  is,
 *                      system calls such as read(2) and pread(2).
 *
 *               syscw: write syscalls
 *                      Attempt  to  count the number of write I/O operations—that is,
 *                      system calls such as write(2) and pwrite(2).
 *
 *               read_bytes: bytes read
 *                      Attempt to count the number of bytes which this process really
 *                      did cause to be fetched from the storage layer.  This is accu‐
 *                      rate for block-backed filesystems.
 *
 *               write_bytes: bytes written
 *                      Attempt to count the number of bytes which this process caused
 *                      to be sent to the storage layer.
 *
 *               cancelled_write_bytes:
 *                      The  big inaccuracy here is truncate.  If a process writes 1MB
 *                      to a file and then deletes the file, it will in  fact  perform
 *                      no writeout.  But it will have been accounted as having caused
 *                      1MB of write.  In other words: this field represents the  num‐
 *                      ber of bytes which this process caused to not happen, by trun‐
 *                      cating pagecache.  A task can cause "negative"  I/O  too.   If
 *                      this  task  truncates  some  dirty  pagecache,  some I/O which
 *                      another task has been accounted for (in its write_bytes)  will
 *                      not be happening.
 * </pre>
 * @date 2020/3/31 21:28
 * @Version 1.0
 **/
public class ProcessIo implements Serializable {
    private static final long serialVersionUID = 5001306485898895819L;
    private List<FileDescriptor> fds;
    private long rChar;
    private long wChar;
    private long sysCallReadCount;
    private long sysCallWriteCount;
    private long readBytes;
    private long writeBytes;
    private long cancelledWriteBytes;

    public List<FileDescriptor> getFds() {
        return fds;
    }

    public void setFds(List<FileDescriptor> fds) {
        this.fds = fds;
    }

    public long getrChar() {
        return rChar;
    }

    public void setrChar(long rChar) {
        this.rChar = rChar;
    }

    public long getwChar() {
        return wChar;
    }

    public void setwChar(long wChar) {
        this.wChar = wChar;
    }

    public long getSysCallReadCount() {
        return sysCallReadCount;
    }

    public void setSysCallReadCount(long sysCallReadCount) {
        this.sysCallReadCount = sysCallReadCount;
    }

    public long getSysCallWriteCount() {
        return sysCallWriteCount;
    }

    public void setSysCallWriteCount(long sysCallWriteCount) {
        this.sysCallWriteCount = sysCallWriteCount;
    }

    public long getReadBytes() {
        return readBytes;
    }

    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }

    public long getWriteBytes() {
        return writeBytes;
    }

    public void setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
    }

    public long getCancelledWriteBytes() {
        return cancelledWriteBytes;
    }

    public void setCancelledWriteBytes(long cancelledWriteBytes) {
        this.cancelledWriteBytes = cancelledWriteBytes;
    }
}
