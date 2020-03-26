package com.kafka;

import com.alibaba.fastjson.JSON;
import com.cpu.StackNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-28 17:18
 **/
public class KafkaConsumerTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        /* 定义kakfa 服务的地址，不需要将所有broker指定上 */
        props.put("bootstrap.servers", "kafka.dev.perfma-inc.net:9092");
        /* 制定consumer group */
        props.put("group.id", "test");
        /* 是否自动确认offset */
        props.put("enable.auto.commit", "true");
        /* 自动确认offset的时间间隔 */
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        /* key的序列化类 */
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /* value的序列化类 */
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /* 定义consumer */
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        /* 消费者订阅的topic, 可同时订阅多个 */
        consumer.subscribe(Arrays.asList("test_topic", "bar"));

        /* 读取数据，读取超时时间为100ms */
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                if (record.value() != null) {
                    StackNode sn = JSON.parseObject(record.value(), StackNode.class);
                    System.out.println(sn);
                }
            }
        }
    }

}
