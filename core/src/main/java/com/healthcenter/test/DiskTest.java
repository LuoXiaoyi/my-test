package com.healthcenter.test;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/26 18:51
 * @Version 1.0
 **/
public class DiskTest {
    static long MB = 1024 * 1024;
    static long GB = 1024 * MB;
    public static void main(String[] args) throws Exception {
        List<FileSystemProvider> fsProviders = FileSystemProvider.installedProviders();
        System.out.println(fsProviders.size());
        for (FileSystemProvider fsProvider : fsProviders) {
        }

        FileSystem fs = FileSystems.getDefault();
        Iterable<FileStore> stores = fs.getFileStores();
        for (FileStore store : stores) {
            System.out.println("---------------------------- ---------------------------- ");
            System.out.println("name: " + store.name() + ", type: " + store.type());
            System.out.println("total: " + store.getTotalSpace() / GB + " G");
            System.out.println("usable: " + store.getUsableSpace() / GB + " G");
        }
    }
}
