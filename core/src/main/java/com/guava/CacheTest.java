package com.guava;

import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/8 16:27
 **/
public class CacheTest {
    static CacheLoader<String, String> cacheLoader;

    static {
        cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                /*System.out.println("load value for key : " + key);
                return key + "->value";*/
                return null;
            }
        };

    }

    public static void main(String[] args) {
        testCacheOnTime();
    }

    /**
     * expireAfterAccess: 指的是在访问过某个 key 之后经过多长时间，该值失效
     *                  evict records based on their idle time.
     * expireAfterWrite：指的是某个key 的值，在第一次加载后，多久会失效
     *                  evict records based on their total live time.
     */
    static void testCacheOnTime(){

        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2,TimeUnit.SECONDS)
//                .expireAfterWrite(2,TimeUnit.SECONDS)
                .removalListener((notification) -> {
                    if (notification != null) {
                        System.out.println(notification.getKey() + " : " + notification.getValue() + " removed");
                        System.out.println("cause: " + notification.getCause().toString());
                    }
                }).build(cacheLoader);
        try {
            cache.get("hello");
            cache.getUnchecked("luoxiaoyi0");
        }catch (Exception e){
            System.out.println("error.");
            e.printStackTrace();
        }

        try {
            System.out.println("going to sleep...");
            Thread.sleep(3000);
            System.out.println("end to sleep...");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cache.getUnchecked("luoxiaoyi0");

        cache.getUnchecked("luoxiaoyi1");

        System.out.println("second get key luoxiaoyi1");

        cache.getUnchecked("luoxiaoyi1");

        try {
            int i = 0;
            while (i < 10) {
                Thread.sleep(200);
                cache.getUnchecked("luoxiaoyi1");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("third get key luoxiaoyi1");

        cache.getUnchecked("luoxiaoyi1");
    }

    static void testCacheOnWeight() {

        Weigher<String, String> weigher = (key, value) -> {

            int w = key.length() % 100;
            System.out.println("w for key " + key + " is: " + w);
            return w;
        };
        /**
         * 默认情况下，cache 中的 segment 的数量和 concurrentLevel 的数量是一样的，即默认的4
         * 这样，设置的 maxinumWeight 将会被除以4，于是，每个 segment 的 weight 就是20，
         * 当某个 segment 的所有 entry 的 weight 之后大于自身的总 weight 时，就会开始在当前的 segment 下删除链表头的第一个元素
         */
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().weigher(weigher).maximumWeight(99)
                .removalListener((notification) -> {
                    if (notification != null) {
                        System.out.println(notification.getKey() + " : " + notification.getValue() + " removed");
                        System.out.println("cause: " + notification.getCause().toString());
                    }
                }).build(cacheLoader);

        cache.getUnchecked("luoxiaoyi0");
        cache.getUnchecked("luoxiaoyi1");
        cache.getUnchecked("luoxiaoyi2");
        cache.getUnchecked("luoxiaoyi3");
        cache.getUnchecked("luoxiaoyi4");
        cache.getUnchecked("luoxiaoyi5");
        /*cache.getUnchecked("luoxiaoyi6");
        cache.getUnchecked("luoxiaoyi7");
        cache.getUnchecked("luoxiaoyi8");
        cache.getUnchecked("luoxiaoyi9");*/


        System.out.println(cache.size());
    }

    static void testCacheOnSize() {


        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(10)
                .removalListener(new RemovalListener<String, String>() {

                    public void onRemoval(RemovalNotification<String, String> notification) {
                        if (notification != null) {

                            System.out.println(notification.getKey() + " : " + notification.getValue() + " removed");
                            System.out.println("cause: " + notification.getCause().toString());
                        }
                    }
                }).build(cacheLoader);

        try {
            cache.get("hahaha");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        cache.getUnchecked("luoxiaoyi");
        cache.getUnchecked("luoxiaoyi1");
        cache.getUnchecked("luoxiaoyi2");

        cache.getUnchecked("luoxiaoyi");
        cache.getUnchecked("luoxiaoyi3");

        System.out.println(cache.size());

        cache.getUnchecked("luoxiaoyi");
    }

}
