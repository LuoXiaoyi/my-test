package com;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/16 21:21
 * @Version 1.0
 **/
public class ProtocolParser {

    // 1 --> jvmUptime
    // 2 --> s0Capacity
    // 20

    // int:type[:int]:value
    // type: 0--> int, 1 --> long,  2 --> double,3 --> string
    static boolean parse(byte[] bytes){
        // 默认采用大端
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        int version = buffer.getInt();
        if(version == 1) {
            while (buffer.hasRemaining()) {
                // key
                int key = buffer.getInt();

                // gc
                if (key > 0 && key < 20) {
                    int type = buffer.getInt();
                    switch (type) {
                        case 0: {
                            int value = buffer.getInt();
                            break;
                        }
                        case 1: {
                            long value = buffer.getLong();
                        }
                        case 2: {
                            double value = buffer.getDouble();
                        }
                        case 3:
                            int len = buffer.getInt();
                            byte[] value = new byte[len];
                            buffer.get(value);
                    }
                }
            }
        } else if(version == 2){
        }

        return false;
    }
}
