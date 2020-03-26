package com.thread;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-04 14:32
 **/
public class ScheduleExecutorTest {
//-agentpath:/Applications/YourKit-Java-Profiler-2019.8.app/Contents/Resources/bin/mac/libyjpagent.dylib=delay=10000
    public static void main(String[] args) {

        ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(2);

        ses.scheduleAtFixedRate(() -> {
            System.out.println("1....");

        }, 0, 1000, TimeUnit.MILLISECONDS);

        ses.scheduleAtFixedRate(() -> {
            System.out.println("2....");
            throw new RuntimeException("error");
        }, 0, 1000, TimeUnit.MILLISECONDS);

    }

}
