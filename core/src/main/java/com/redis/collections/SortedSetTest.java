package com.redis.collections;

import com.redis.RedisClientManager;
import com.redis.objects.ThreadPoolItem;
import com.redis.objects.ThreadPoolVo;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RSortedSet;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/21 21:01
 **/
public class SortedSetTest {
    public static void main(String[] args) {
        //deleteTest();
        // rsetTest();
        // expireTest();
        // updateScoredSortedSetTest();
        rScoredSortedSetTest();
        //  rSortedSetTest();
    }

    private static void deleteTest() {
        RKeys keys = RedisClientManager.redission().getKeys();
        keys.deleteByPattern("*t_s*");
        RedisClientManager.redission().shutdown();
    }

    private static void expireTest() {
        RKeys keys = RedisClientManager.redission().getKeys();
        Iterator<String> kStrs = keys.getKeysByPattern("*t_s*").iterator();
        while (kStrs.hasNext()) {
            String abc = kStrs.next();
            System.out.println(abc);
            keys.expire(abc, 10, TimeUnit.SECONDS);
        }
        RedisClientManager.redission().shutdown();
    }

    private static void rsetTest() {
        RSet<String> rSet = RedisClientManager.redission().getSet("test_set");
        rSet.add("3000");
        rSet.add("4000");
        /*System.out.println(rSet.iterator().next());
        rSet.remove("1000");
        System.out.println(rSet.iterator().next());*/
        RedisClientManager.redission().shutdown();
    }

    private static void updateScoredSortedSetTest() {
        RScoredSortedSet<ThreadPoolVo> rScoredSortedSet =
                RedisClientManager.redission().getScoredSortedSet("pool-score-sorted-set-key");
        Collection<ThreadPoolVo> vos = rScoredSortedSet.valueRange(6, true, 14, false);
        for (ThreadPoolVo vo : vos) {
            System.out.println(vo);
            vo.setName(vo.getName() + "haha");
        }
        RedisClientManager.redission().shutdown();
    }

    private static void rScoredSortedSetTest() {
        RScoredSortedSet<ThreadPoolVo> rScoredSortedSet =
                RedisClientManager.redission().getScoredSortedSet("pool-score-sorted-set-key");

        boolean a = rScoredSortedSet.add(11232, buildThreadPoolVo(4));
        System.out.println(a);

        a = rScoredSortedSet.add(12232, buildThreadPoolVo(5));
        System.out.println(a);

        a = rScoredSortedSet.add(13232, buildThreadPoolVo(6));
        System.out.println(a);

        /*rScoredSortedSet.clear();
        StopWatch watch = new StopWatch();
        watch.start();
        int n = 0;
        for (int j = 0; j < 100; j++) {
            Map<ThreadPoolVo, Double> values = new HashMap<>();
            for (int i = 0; i < 100; i++) {
                values.put(buildThreadPoolVo(n), n * 10.0);
                n++;
            }
            rScoredSortedSet.addAll(values);
        }
        watch.stop();

        System.out.println("ms: " + watch.getTime());
        System.out.println("size: " + rScoredSortedSet.size());
        System.out.println(" first: " + rScoredSortedSet.first());
        System.out.println(" last: " + rScoredSortedSet.last());
        Collection<ThreadPoolVo> vos = rScoredSortedSet.valueRange(0, 10000);
        *//*Collection<ThreadPoolVo> vos = rScoredSortedSet.valueRange(6, true, 14, false);
        for (ThreadPoolVo vo : vos) {
            System.out.println(vo);
        }*/

        RedisClientManager.redission().shutdown();
    }

    private static void rSortedSetTest() {
        RSortedSet<ThreadPoolVo> sortedSet =
                RedisClientManager.redission().getSortedSet("pool-sorted-set-key");
        sortedSet.add(buildThreadPoolVo(1));
        sortedSet.add(buildThreadPoolVo(1));
        Iterator<ThreadPoolVo> vos = sortedSet.iterator();
        while (vos.hasNext()) {
            System.out.println(vos.next());
        }
    }

    private static ThreadPoolVo buildThreadPoolVo(int seq) {
        ThreadPoolVo poolVo = new ThreadPoolVo();
        poolVo.setName("test" + seq);
        poolVo.setAccCnt(seq * 2);
        poolVo.setCrtCnt(seq);

        List<ThreadPoolItem> addItems = new LinkedList<>();
        ThreadPoolItem item = new ThreadPoolItem();
        item.setThreadName("test1-1");
        item.setTid(seq * 12323L);
        addItems.add(item);

        List<ThreadPoolItem> delItems = new LinkedList<>();
        item = new ThreadPoolItem();
        item.setThreadName("test1-2");
        item.setTid((seq + 1) * 12323L);
        delItems.add(item);

        poolVo.setAdd(addItems);
        poolVo.setDel(delItems);
        return poolVo;
    }
}
