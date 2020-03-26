package com.redis;

import com.redis.objects.ThreadInfo;
import org.apache.commons.lang3.time.StopWatch;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/18 13:55
 **/
public class FirstTest {

    private static RedissonClient redissonClient;

    static {
        Config config = new Config();
        /*try {
            config = Config.fromJSON("classpath:redis-single-config.json");
        } catch (IOException e) {
            throw new InternalError("builder redis config error.", e);
        }

        config.useClusterServers()
                .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
                //可以用"rediss://"来启用SSL连接
                .addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001")
                .addNodeAddress("redis://127.0.0.1:7002");*/
        config.useSingleServer().setAddress("redis://redis.dev.perfma-inc.net:6379");
        // config.setTransportMode(TransportMode.KQUEUE);
        // fst 依赖

        config.setCodec(new org.redisson.codec.FstCodec());
        // config.setCodec(new org.redisson.codec.LZ4Codec());
        // config.setCodec(new org.redisson.codec.SnappyCodec());
        // redissonClient = Redisson.create(config);
        /*try {
            System.out.println(config.toYAML());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        redissonClient = RedisClientManager.redission();
    }

    public static void main(String[] args) {
        try {
            testMap();
            // testObject();
            // testString();
            // testObject();
            //testList();
        } finally {
            redissonClient.shutdown();
        }
    }

    public static void testMap() {
        RMap<String, List<String>> rabc = redissonClient.getMap("xiluo-test-map-2");
        System.out.println(rabc.get("1"));
       /* Map<String, List<String>> abc = new HashMap<>();
        abc.put("1", Lists.newArrayList("abc", "efg"));
        abc.put("2", Lists.newArrayList("abc", "efg"));
        abc.put("3", Lists.newArrayList("abc", "efg"));
        abc.put("4", Lists.newArrayList("abc", "efg"));

        rabc.putAll(abc);*/
    }

    /**
     * 存取 1000000 条
     * default: org.redisson.codec.JsonJacksonCodec()  5840 ms  -- 139108 Kb
     * <p>
     * org.redisson.codec.FstCodec() 3739 ms  -- 70116 Kb / 默认的二分之一，时间上还快那么一点，约为原来的0.71  ---++++++ 似乎是最佳的
     * org.redisson.codec.LZ4Codec() 6050 ms  -- 76184 Kb / 默认的二分之一，但是写入的时间和原来差不多
     * org.redisson.codec.SnappyCodec() 5199 ms -- 77156 Kb/ 默认的二分之一，写入时间和原来差不多
     */
    private static void testList() {
        StopWatch stopWatch = new StopWatch();
        RList<ThreadInfo> firstBucket = redissonClient.getList("THREAD-LIST-KEY");
        stopWatch.start();
        System.out.println(firstBucket.get(0));
        System.out.println(firstBucket.get(1));
        System.out.println(firstBucket.get(2));
        System.out.println(firstBucket.getName());
        /*firstBucket.clear();
        List list = new LinkedList();
        for(int i = 0;i < 1000000; ++i){
            ThreadInfo ti = new ThreadInfo();
            ti.tid = i;
            ti.lwpId = i * 10000l;
            ti.name = "T-" + i;
            ti.timeStamp = System.currentTimeMillis();
            list.add(ti);
            //firstBucket.add(ti); // 每次都会建立一次连接
        }
        firstBucket.addAll(list); // 只会创建一次连接*/
        stopWatch.stop();
        logger.info("cost: {} ms", stopWatch.getTime());
    }

    /**
     * org.redisson.codec.JsonJacksonCodec 和 org.redisson.codec.FstCodec 相比
     * 后者在存储上会占更小的体积，对于 null 或者 0 的数值会直接忽略掉，而 JSON 只会对 null 值忽略
     * org.redisson.codec.LZ4Codec org.redisson.codec.SnappyCodec
     */
    private static void testObject() {
        RBucket<ThreadInfo> firstBucket = redissonClient.getBucket("THREAD-KEY");
        /*firstBucket.expire(10, TimeUnit.SECONDS);

        RKeys keys = redissonClient.getKeys();

        Iterator<String> kstrs = keys.getKeysByPattern("pool-*").iterator();

        // Iterator<String> kstrs = keys.getKeys().iterator();
        while (kstrs.hasNext()) {

            System.out.println(kstrs.next());
        }*/
        // firstBucket.delete();
        /*ThreadInfo ti = new ThreadInfo();
        ti.name = "Thread-2";
        ti.taskId = "task-111";
        ti.lwpId = 10000l;
        firstBucket.set(ti);*/
        ThreadInfo ti = firstBucket.get();
        System.out.println(ti);
    }

    private static void testString() {
        RBucket<String> firstBucket = redissonClient.getBucket("first-test-key");
        //firstBucket.set("first-test-value");
        System.out.println(firstBucket.get());
    }

    private static Logger logger = LoggerFactory.getLogger(FirstTest.class);
}
