package com.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-04 18:10
 **/
public class FileAttrTest {

    public static void main(String[] args) {

        Path path = Paths.get("/Users/xiaoyiluo/Desktop/common-error.log");

        FileOwnerAttributeView foav = Files.getFileAttributeView(path,
                FileOwnerAttributeView.class);
        try {
            UserPrincipal owner = foav.getOwner();
            System.out.format("Original owner  of  %s  is %s%n", path,
                    owner.getName());

            FileSystem fs = FileSystems.getDefault();
            UserPrincipalLookupService upls = fs.getUserPrincipalLookupService();

            UserPrincipal newOwner = upls.lookupPrincipalByName("abc");
            foav.setOwner(newOwner);

            UserPrincipal changedOwner = foav.getOwner();
            System.out.format("New owner  of  %s  is %s%n", path,
                    changedOwner.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
