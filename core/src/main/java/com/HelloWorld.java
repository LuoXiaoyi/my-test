package com;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-11-07 16:08
 **/
public class HelloWorld {

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        System.out.println(sdf.format(new Date(System.currentTimeMillis())));
        //testString();

        /*Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    System.out.println("in: " + Thread.currentThread().isInterrupted());
                }
            }
        });
        t.start();

        Thread.sleep(1000);
        System.out.println("begin to interrupt t");
        t.interrupt();
        System.out.println("out: " + t.isInterrupted());

        Thread.sleep(2000);
        t.interrupt();
        System.out.println("out: " + t.isInterrupted());

        t.join();*/
        //testMap();
    }

    static void testString() {
        String fp = "abc:123:3434:9034";
        int beginIdx = 0, length = fp.length();
        for (int i = 0; i < length; ++i) {
            if (fp.charAt(i) == ':' || i == length - 1) {
                System.out.println(fp.substring(beginIdx, i == length - 1 ? i + 1 : i));
                // 指向 ':' 的后一个字符
                beginIdx = i + 1;
            }
        }
    }

    static void testMap() throws Exception {

        Map<String, String> test = new ConcurrentHashMap<>();

        for (int i = 0; i < 100000; ++i) {
            test.put("key" + i, "value" + i * 10);
        }

        Thread[] threads = new Thread[4];
        for (int j = 0; j < 4; j++) {
            threads[j] = new Thread(() -> {
                Iterator<Map.Entry<String, String>> iter = test.entrySet().iterator();
                while (iter.hasNext()) {
                    test.remove(iter.next().getKey());
                }
            });

            threads[j].start();
        }

        threads[0].join();
        threads[1].join();
        threads[2].join();
        threads[3].join();
        System.out.println(test);
    }

    static void test(String[] args) {
        String abc = "-javaagent:<jarpath>=XOWL_HOME=/home/admin/perfma/xowl,LOG_LEVEL=INFO,DATA_ROOT_DIR=";
        String part = abc.substring(abc.indexOf("DATA_ROOT_DIR=") + "DATA_ROOT_DIR=".length());
        System.out.println(part.length());
        if (part.length() > 0) {
            int endPos = 0;
            while (endPos < part.length() && part.charAt(endPos) != ',' && part.charAt(endPos) != ' ') {
                endPos++;
            }
            part = part.substring(0, endPos);
            System.out.println(part);
        }

        String[] r = "abcd     ad a   da d  ".split(" ");
        System.out.println(Arrays.asList(r));
        String[] str = {
                "openjdk version \"1.8.0_232\"",
                "OpenJDK Runtime Environment (build 1.8.0_232-b09)",
                "Eclipse OpenJ9 VM (build openj9-0.17.0, JRE 1.8.0 Linux amd64-64-Bit Compressed References 20191017_442 (JIT enabled, AOT enabled)",
                "OpenJ9   - 77c1cf708",
                "OMR      - 20db4fbc",
                "JCL - 97b5ec8f383 based on jdk8u232 - b09)"
        };

        String[] str2 = {"java version \"1.7.0_80\"",
                "Java(TM) SE Runtime Environment (build 1.7.0_80-b15)",
                "Java HotSpot(TM) 64-Bit Server VM (build 24.80-b11, mixed mode)"};

        String[] str3 = {
                "java version \"1.8.0_221\"",
                "Java(TM) SE Runtime Environment (build 8.0.5.41 - pxa6480sr5fp41-20190919_01(SR5 FP41))",
                "IBM J9 VM (build 2.9, JRE 1.8.0 Linux amd64-64-Bit Compressed References 20190911_427071 (JIT enabled, AOT enabled)",
                "OpenJ9   - d581d49",
                "OMR      - ca4db84",
                "IBM      - 5cfdf9c)",
                "JCL - 20190918_01 based on Oracle jdk8u221-b11"
        };

        String version = null, jvmType = null;
        for (String s : str3) {
            s = s.toLowerCase();
            if (s.contains("version") && s.contains("\"")) {
                version = s.substring(s.indexOf('"') + 1, s.lastIndexOf('"'));
            } else if (s.contains("ibm") && s.contains("j9")) {
                jvmType = "IBMJ9";
            } else if (s.contains("openj9")) {
                jvmType = "OpenJ9";
            } else if (s.contains("hotspot")) {
                jvmType = "HotSpot";
            }
            if (version != null && jvmType != null) {
                break;
            }
        }

        if (jvmType == null) {
            jvmType = "unknown";
        }

        System.out.println(jvmType + ":" + version);
    }
}
