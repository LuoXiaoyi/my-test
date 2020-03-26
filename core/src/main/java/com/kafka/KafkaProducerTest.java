package com.kafka;

import com.alibaba.fastjson.JSON;
import com.cpu.MergeUtil;
import com.cpu.ReadBinaryTest;
import com.cpu.StackNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-28 17:18
 **/
public class KafkaProducerTest {

    public static void main(String[] args) throws Exception {

        System.out.println(key(0, true, null));
        //sendNormal(2);
        //sendTrigger();

        /*long orgTime = System.currentTimeMillis();
        int mesh = 60000;
        System.out.println(orgTime + ":" + align(orgTime, mesh));*/

    }

    private static String key(int stackId, boolean root, String parentSlotId) {
        StringBuilder key = new StringBuilder();
        if (root) {
            key.append("true").append(':').append(parentSlotId);
        } else {
            key.append(stackId).append(":").append("false").append(':').append(parentSlotId);
        }

        return key.toString();
    }

    /**
     * 时间对齐，orgTime 时间按 mesh 进行对齐，并返回对齐结果
     *
     * @param orgTime 原始时间，毫秒
     * @param mesh    对齐粒度，毫秒
     * @return 对齐结果
     */
    public static long align(long orgTime, int mesh) {
        return mesh == 0 ? orgTime : orgTime - (orgTime % mesh);
    }

    public static void sendTrigger() throws Exception {
        StackNode sn = new StackNode(-1);
        sn.setTaskId("task-id-abc");
        sn.setMesh(-1);
        sn.setTimeStamp(System.currentTimeMillis());
        Producer<String, String> kafkaProducer = kafkaMQProducer();
        String str = JSON.toJSONString(sn);
        System.out.println(str);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "xiluo", str);
        kafkaProducer.send(record).get();
        System.out.println("send OK");
    }

    public static void sendNormal(int cnt) throws Exception {
        List<StackNode> sns3 = null;
        for (int i = 0; i < 1; ++i) {
            String filePath = "/Users/xiaoyiluo/wKgAQlzr6d2EITSSAAAAAAAAAAA.rt-cpu";
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();
            List<StackNode> sns1 = ReadBinaryTest.parse(buf);
            sns3 = MergeUtil.mergeStackNodes(sns1);
        }

        Producer<String, String> kafkaProducer = kafkaMQProducer();
        StackNode sn = sns3.get(0);
        sn.setTaskId("task-id-abc-0987654");
        for (int i = 0; i < cnt; ++i) {
            Thread.sleep(5000);
            sn.setTimeStamp(System.currentTimeMillis());
            sn.setMesh(5);
            String str = JSON.toJSONString(sn);
            System.out.println("str: " + str);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "xiluo", str);
            kafkaProducer.send(record).get();
            System.out.println("send OK");
        }
    }

    public static Producer<String, String> kafkaMQProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "kafka.dev.perfma-inc.net:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(properties);
    }

    static String topic = "hot_method_merge_topic";
}
