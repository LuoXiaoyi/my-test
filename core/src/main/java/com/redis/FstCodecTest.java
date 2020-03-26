package com.redis;

import com.alibaba.fastjson.JSON;
import com.redis.objects.ThreadInfo;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/18 17:47
 **/
public class FstCodecTest {

    private static final int COUNT = 10000;

    public static void main(String[] args) throws Exception {
        write();
        writeJson();
        read();
    }

    private static void read() throws Exception {
        FSTConfiguration fst = FSTConfiguration.getDefaultConfiguration();
        FileInputStream fos = new FileInputStream(new File("/Users/xiaoyiluo/Documents/my-space/code/test/ti-test.txt"));
        FSTObjectInput fstObjectInput = fst.getObjectInput(fos);
        while (fstObjectInput.available() > 0) {
            Object obj = fstObjectInput.readObject();
            // System.out.println(obj.getClass());
        }
        fstObjectInput.close();
    }

    /**
     * 大小几乎是 FST 的两倍 ，这个大小是： 7.0 MB
     *
     * @throws IOException
     */
    private static void writeJson() throws IOException {
        FileOutputStream fos = new FileOutputStream(
                new File("/Users/xiaoyiluo/Documents/my-space/code/test/ti-test-json.txt"));
        long s = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            ThreadInfo ti = new ThreadInfo();
            ti.tid = i;
            ti.lwpId = i * 10000l;
            ti.name = "T-" + i;
            ti.timeStamp = System.currentTimeMillis();
            fos.write(JSON.toJSONBytes(ti));
        }
        long e = System.currentTimeMillis();
        fos.flush();
        fos.close();
        System.out.println("JSON cost: " + (e - s) + " ms");
    }

    /**
     * 大小：3.8 MB
     *
     * @throws Exception
     */
    private static void write() throws Exception {
        FSTConfiguration fst = FSTConfiguration.getDefaultConfiguration();

        FileOutputStream fos = new FileOutputStream(new File("/Users/xiaoyiluo/Documents/my-space/code/test/ti-test.txt"));
        FSTObjectOutput fstObjectOutput = fst.getObjectOutput(fos);
        long s = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            ThreadInfo ti = new ThreadInfo();
            ti.tid = i;
            ti.lwpId = i * 10000l;
            ti.name = "T-" + i;
            ti.timeStamp = System.currentTimeMillis();

            fstObjectOutput.writeObject(ti);
        }
        long e = System.currentTimeMillis();
        fstObjectOutput.flush();
        fstObjectOutput.close();
        System.out.println("FST cost: " + (e - s) + " ms");
    }
}
