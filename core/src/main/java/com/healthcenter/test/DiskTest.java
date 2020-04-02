package com.healthcenter.test;

import java.lang.reflect.Method;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

/**
 * @author xiluo
 * @date 2020/3/26 18:51
 * @Version 1.0
 **/
public class DiskTest {
    static long MB = 1024 * 1024;
    static long GB = 1024 * MB;
    public static void main(String[] args) throws Exception {
        Iterable<FileStore> fileStores = FileSystems.getDefault().getFileStores();
        for (FileStore fileStore : fileStores) {
            System.out.println("fs name: " + fileStore.name());

            Class<?> ufsClz = Class.forName("sun.nio.fs.UnixFileStore");
            if (ufsClz.isAssignableFrom(fileStore.getClass())) {
                Method entry = ufsClz.getDeclaredMethod("entry");
                entry.setAccessible(true);
                Object eObj = entry.invoke(fileStore);
                Method dir = eObj.getClass().getDeclaredMethod("dir");
                dir.setAccessible(true);
                byte[] dirBytes = (byte[]) dir.invoke(eObj);
                if(dirBytes != null){
                    System.out.println("mounted on: " + new String(dirBytes));
                }
            }

            System.out.println("fs type: " + fileStore.type());
            System.out.println("fs total space: " + fileStore.getTotalSpace());
            System.out.println("fs unused space: " + fileStore.getUsableSpace());
            System.out.println("fs unallocated space: " + fileStore.getUnallocatedSpace());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }
}
