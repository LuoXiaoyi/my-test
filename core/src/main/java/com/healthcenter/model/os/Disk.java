package com.healthcenter.model.os;

import java.io.Serializable;

/**
 * @author xiluo
 * @description
 * <pre>
 *     参考 df 的实现，这里的每个值都来自 df 的结果
 *     xiluo@cppdev ~ $ df -lh
 *     Filesystem               Size  Used Avail Use% Mounted on
 *     devtmpfs                 1.9G     0  1.9G   0% /dev
 *     tmpfs                    1.9G     0  1.9G   0% /dev/shm
 *     tmpfs                    1.9G  164M  1.7G   9% /run
 *     tmpfs                    1.9G     0  1.9G   0% /sys/fs/cgroup
 *     /dev/mapper/centos-root   35G   16G   20G  44% /
 *     /dev/vda1               1014M  200M  815M  20% /boot
 *     tmpfs                    380M     0  380M   0% /run/user/1002
 *     tmpfs                    380M     0  380M   0% /run/user/0
 *     tmpfs                    380M     0  380M   0% /run/user/1000
 *     tmpfs                    380M     0  380M   0% /run/user/1003
 *     tmpfs                    380M     0  380M   0% /run/user/1004
 *     
 *     执行命令查看详情：info coreutils 'df invocation'
 *     实现方式可以有两种种，建议第二种：
 *     1. 程序实现可以使用 statfs 系统调用来获取
 *     2. 实现也可以参考 JVM 实现 Java_sun_nio_fs_BsdNativeDispatcher_getfsstat
 *     同时也可以使用 Nio 的相关接口获取
 *     Iterable<FileStore> fileStores = FileSystems.getDefault().getFileStores();
*      for (FileStore fileStore : fileStores) {
*          System.out.println("fs name: " + fileStore.name());
*          Class<?> ufsClz = Class.forName("sun.nio.fs.UnixFileStore");
*          if (ufsClz.isAssignableFrom(fileStore.getClass())) {
*              Method entry = ufsClz.getDeclaredMethod("entry");
*              entry.setAccessible(true);
*              Object eObj = entry.invoke(fileStore);
*              Method dir = eObj.getClass().getDeclaredMethod("dir");
*              dir.setAccessible(true);
*              byte[] dirBytes = (byte[]) dir.invoke(eObj);
*              if(dirBytes != null){
*                  System.out.println("mounted on: " + new String(dirBytes));
*              }
*          }
*          System.out.println("fs type: " + fileStore.type());
*          System.out.println("fs total space: " + fileStore.getTotalSpace());
*          System.out.println("fs unused space: " + fileStore.getUsableSpace());
*          System.out.println("fs unallocated space: " + fileStore.getUnallocatedSpace());
*          System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
*      }
 * </pre>
 * @date 2020/3/25 18:24
 * @Version 1.0
 **/
public class Disk implements Serializable {
    private static final long serialVersionUID = -6428477780196461198L;
    private String fileSystem;
    private long size;
    private long used;
    private long avail;
    private double usePercent;
    private String mountedOn;

    public String getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getAvail() {
        return avail;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }

    public double getUsePercent() {
        return usePercent;
    }

    public void setUsePercent(double usePercent) {
        this.usePercent = usePercent;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public void setMountedOn(String mountedOn) {
        this.mountedOn = mountedOn;
    }
}
