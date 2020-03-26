package com.redis.locks;


import com.redis.RedisClientManager;
import org.redisson.api.RSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/22 16:26
 **/
public class LockTest {
    static List<String> mynames = new LinkedList<>();
    public static void main(String[] args) {
        RSet<String> names = RedisClientManager.redission().getSet("name-set");
        names.delete();

        RedisClientManager.redission().getKeys();
/*
        names.clear();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for(int i = 0;i < 10; ++i){
            executor.execute(new UpdateTask("lxy" + i));
        }

        names = RedisClientManager.redission().getSet("name-set");*/
        System.out.println("size: " + names.size());
        Iterator<String> iterator = names.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        // RedisClientManager.redission().shutdown();
    }

    private static class UpdateTask implements Runnable {
        String name;
        public UpdateTask(String name){
            this.name = name;
        }
        @Override
        public void run() {
            RSet<String> names = RedisClientManager.redission().getSet("name-set");
            names.add(name);
            System.out.println(Thread.currentThread().getName() + "add " + name);
            /*
            RLock lock = RedisClientManager.redission().getReadWriteLock("test-lock").writeLock();
            lock.lock();
            try {
                RSet<String> names = RedisClientManager.redission().getSet("name-set");
                names.add(name);
                System.out.println(Thread.currentThread().getName() + "add " + name);
            } finally {
                lock.unlock();
            }
            */
        }
    }
}
