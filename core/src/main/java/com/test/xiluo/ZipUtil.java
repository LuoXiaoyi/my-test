package com.test.xiluo;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/6/3 22:23
 * @Version 1.0
 **/
public class ZipUtil {

    private static final int BUFFER = 8192;

    /**
     * 压缩
     *
     * @param srcPath
     * @param dstPath
     * @throws IOException
     */
    public static void compress(String srcPath, String dstPath) throws IOException {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcPath + "不存在！");
        }

        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } finally {
            if (null != zipOut) {
                zipOut.close();
                out = null;
            }

            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 解压
     *
     * @param zipFile
     * @param dstPath
     * @throws IOException
     */
    public static void decompress2(String zipFile, String dstPath) throws IOException {
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                ;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            }
        }
        zip.close();
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        } finally {
            if (null != bis) {
                bis.close();
            }
        }
    }

    /**
     * 执行 zip 压缩，最终的效果是将 srcFiles 全部压缩到 srcDir 目录下，同时被写入 zipFile 中
     * 比如：
     * srcFiles 有如下文件：Abc.java Efg.java Hij.java
     * zipFile 为：aaa.zip
     * srcDir 为：bbb
     * 则 Abc.java Efg.java Hij.java 全部被压入 aaa.zip，并且都被至于 bbb 目录下
     *
     * @param zipFile  zip 目标文件
     * @param srcDir   目录名
     * @param srcFiles 被压缩的具体文件
     * @throws IOException
     */
    public static void zipFiles(File zipFile, String srcDir, File... srcFiles) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File inputFile : srcFiles) {
                zos.putNextEntry(new ZipEntry(srcDir + File.separator + inputFile.getName()));
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile))) {
                    int cnt;
                    while ((cnt = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, cnt);
                    }
                } finally {
                    zos.closeEntry();
                }
            }
        }
    }

    /**
     * 解压 zipFile 到目录 dstPath
     *
     * @param zipFile 压缩文件
     * @param dstPath 被解压到的目录
     * @throws IOException
     */
    public static void decompress(String zipFile, String dstPath) throws IOException {
        File pathFile = new File(dstPath);
        if (!pathFile.exists() && !pathFile.mkdirs()) {
            throw new RuntimeException("Can not create dir: " + dstPath);
        }

        try (ZipFile zip = new ZipFile(zipFile)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = entries.nextElement();
                InputStream in = null;
                OutputStream out = null;
                try {
                    String outPath = dstPath + File.separator + entry.getName();
                    //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                    if (new File(outPath).isDirectory()) {
                        continue;
                    }

                    //判断路径是否存在,不存在则创建文件路径
                    File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                    if (!file.exists() && !file.mkdirs()) {
                        throw new RuntimeException("Can not create dir: " + file.getAbsolutePath());
                    }

                    in = zip.getInputStream(entry);
                    out = new FileOutputStream(outPath);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                } finally {
                    if (null != in) {
                        in.close();
                    }

                    if (null != out) {
                        out.close();
                    }
                }
            }
        }
    }

    private static final int BUFFER_SIZE = 8092;
}
