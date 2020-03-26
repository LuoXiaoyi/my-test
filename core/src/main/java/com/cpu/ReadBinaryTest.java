package com.cpu;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-23 00:59
 **/
public class ReadBinaryTest {

    static HashMap<Long, String> mid2MName = new HashMap<>();

    public static void main(String[] args) throws Exception {

//        String filePath = "/Users/xiaoyiluo/wKgAQlzlc06EOhmvAAAAAAAAAAA.rt-cpu";
        //     String filePath = "/Users/xiaoyiluo/wKgAQlzmME2EF-7pAAAAAAAAAAA.rt-cpu.xowl";
        //  String filePath = "/Users/xiaoyiluo/wKgAQlzmME2EF-7pAAAAAAAAAAA.rt-cpu.fastdfs";
        //  String filePath = "/Users/xiaoyiluo/wKgAQlzmMHSEXDkyAAAAAAAAAAA.rt-cpu.fastdfs";
        //String filePath = "/Users/xiaoyiluo/wKgAQlzmU3eEHmsmAAAAAAAAAAA.rt-cpu.whole.fastdfs";
//        String filePath = "/Users/xiaoyiluo/just_for_test.1";

        long start = System.currentTimeMillis();
        List<StackNode> sns3 = null;
        for (int i = 0; i < 1; ++i) {
            String filePath = "/Users/xiaoyiluo/wKgAQlzr6d2EITSSAAAAAAAAAAA.rt-cpu";
            /*if(i %2 == 0) {
                filePath = "/Users/xiaoyiluo/just_for_test.8.xowl";
            }else{
                filePath = "/Users/xiaoyiluo/wKgAQlzmU3eEHmsmAAAAAAAAAAA.rt-cpu.whole.fastdfs";
            }*/

            BufferedInputStream is = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();
            List<StackNode> sns1 = parse(buf);
            sns3 = MergeUtil.mergeStackNodes(sns1);
        }
        System.out.println(sns3.size());
        System.out.println("total cost: " + (System.currentTimeMillis() - start));

    }

    public static List<StackNode> parse(byte[] bytes) throws UnsupportedEncodingException {
        if (bytes != null && bytes.length > 0) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int a = 0;
            while (byteBuffer.hasRemaining()) {
                // \r
                byte b = byteBuffer.get();
                long currentTime = byteBuffer.getLong();
                int validNum1 = byteBuffer.getInt();

                List<StackNode> sns = new ArrayList<>(validNum1);
                for (int i = 0; i < validNum1; i++) {
                    StackNode sn = new StackNode();
                    sn.setTimeStamp(currentTime);
                    long samples = byteBuffer.getLong();
                    sn.setSamples(samples);
                    int nframes = byteBuffer.getInt();
                    List<FrameNode> fns = new ArrayList<>(nframes);
                    for (int j = 0; j < nframes; j++) {
                        // mid+mname, total length
                        int totalLen = byteBuffer.getInt();
                        long mid = 0;
                        String mName = null;
                        if (totalLen >= 8) {
                            mid = byteBuffer.getLong();
                        }

                        FrameNode fn = new FrameNode(mid, samples);
                        if (totalLen > 8) {
                            byte[] mNameBytes = new byte[totalLen - 8];
                            byteBuffer.get(mNameBytes);
                            mName = decode(new String(mNameBytes, "UTF-8"));
                        }

                        if (mid > 0 && mName != null) {
                            mid2MName.put(mid, mName);
                            a++;
                        } else {
                            if (!mid2MName.containsKey(mid)) {
                                System.err.println("can not find method name for mid: " + mid);
                            }
                        }

                        fns.add(fn);
                    }

                    sn.setFrames(fns);
                    sns.add(sn);
                }

                // \n
                b = byteBuffer.get();

                System.out.println("total: " + a);
                return sns;
            }

        }

        return null;
    }


    //解密
    private static String decode(String pstr) {
        int[] pkey = {9, 1, 7, 8, 2};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pstr.length(); i++) {
            sb.append(decodeChar(pstr.charAt(i), pkey[i % 5]));
        }
        return sb.toString();
    }

    private static char decodeChar(char c, int key) {
        return (char) (c ^ key);
    }
}
