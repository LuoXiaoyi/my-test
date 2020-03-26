package com.redis.collections;

import com.redis.DefaultLocalCachedMapOptions;
import com.redis.RedisClientManager;
import org.redisson.api.RKeys;
import org.redisson.api.RListMultimap;
import org.redisson.api.RLocalCachedMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/28 19:49
 **/
public class MultimapTest {

    public static void main(String[] args) {
        // create();
        // expire();
        expireKeysByPattern("*abc:12343*");
        //deleteKeysByPattern("abc:12343*");
    }

    private static void deleteKeysByPattern(String keysPattern) {
        RKeys rKeys = RedisClientManager.redission().getKeys();
        rKeys.deleteByPattern(keysPattern);
    }

    /**
     * 根据键的模式匹配来 expire 能匹配上的所有 key
     *
     * @param keysPattern 模式串
     * @return 成功 expire 的 key 的个数
     */
    private static int expireKeysByPattern(String keysPattern) {
        RKeys rKeys = RedisClientManager.redission().getKeys();
        Iterator<String> keyIter = rKeys.getKeysByPattern(keysPattern).iterator();
        int nbr = 0;
        while (keyIter.hasNext()) { // TODO 这里迭代 expire，不知道对 redis 的网络会不会有影响
            // 设置半分钟超时自动删除
            String key = keyIter.next();
            boolean isOk = rKeys.expire(key, 20, TimeUnit.SECONDS);
            if (isOk) {
                nbr++;
                System.out.println("Succeed in expiring redis cache key " + key);
            } else {
                System.out.println("Failed to expire redis cache key " + key);
            }
        }
        RedisClientManager.redission().shutdown();
        return nbr;
    }

    private static void expire() {
        RListMultimap<String, String> listMultimap = RedisClientManager.redission().getListMultimapCache("lxy_test");
        boolean isOk = listMultimap.expire(10, TimeUnit.SECONDS);
        System.out.println("isOK: " + isOk);
        RedisClientManager.redission().shutdown();
    }

    private static void clear() {
        RListMultimap<String, String> listMultimap = RedisClientManager.redission().getListMultimapCache("lxy_test");

        listMultimap.clear();

        RedisClientManager.redission().shutdown();
    }

    private static void create() {
        RLocalCachedMap<String, List<String>> listMultimap =
                RedisClientManager.redission().
                        getLocalCachedMap("abc:12343:lxy_test", DefaultLocalCachedMapOptions.options);
//        RListMultimap<String, String> listMultimap = RedisClientManager.redission().getListMultimapCache("abc:12343:lxy_test");
        List<String> list = new ArrayList<>();
        list.add("abx_value-1");
        list.add("abx_value-0");
        listMultimap.put("abx", list);

        list = new ArrayList<>();
        list.add("abx_value-1-0");
        list.add("abx_value-1-1");
        listMultimap.put("abx1", list);

        list = new ArrayList<>();
        list.add("abx_value-2-1");
        list.add("abx_value-2-0");
        listMultimap.put("abx2", list);

//        listMultimap = RedisClientManager.redission().getListMultimapCache("abc:12343:lxy_test2");

     /*   listMultimap.put("abx", "abx_value");
        listMultimap.put("abx2", "abx_value2");
        listMultimap.put("abx2", "abx_value3");
*/
        RedisClientManager.redission().shutdown();
    }
}
