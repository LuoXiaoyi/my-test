package com.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/21 22:10
 **/
public class RedisClientManager {

    public static void main(String[] args) {
        System.out.println(redissonClient);
    }

    public static RedissonClient redission() {
        return redissonClient;
    }

    private static final RedissonClient redissonClient;

    static {
        // 其他这几种编码方式的一个最大问题，就是序列化之后，如果这个类发生了变化，会导致之前保存在 Redis 里面的对象在被反序列化失败的问题
        // 但是默认的 JSON 是不会出现上述问题的，兼容性最好
        // config.setTransportMode(TransportMode.KQUEUE);
        // fst 依赖
        // config.setCodec(new org.redisson.codec.FstCodec());
        // config.setCodec(new org.redisson.codec.LZ4Codec());
        // config.setCodec(new org.redisson.codec.SnappyCodec());
        try {
            redissonClient = Redisson.create(Config.fromYAML(
                    new File("/Users/xiaoyiluo/Documents/my-space/code/test/src/main/resources/redis-single-config.yaml")));
        } catch (IOException e) {
            throw new InternalError("init redis client error.", e);
        }
    }
}
