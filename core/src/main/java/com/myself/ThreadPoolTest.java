package com.myself;

//import javafx.scene.input.DataFormat;

import java.text.*;
import java.util.Date;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/7 17:31
 **/
public class ThreadPoolTest {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    // Packing and unpacking ctl
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    public static void main(String[] args) {
        System.out.println("COUNT_BITS: " + COUNT_BITS);
        System.out.println("CAPACITY: " + CAPACITY);
        System.out.println("RUNNING: " + RUNNING);
        System.out.println("SHUTDOWN: " + SHUTDOWN);
        System.out.println("STOP: " + STOP);
        System.out.println("TIDYING: " + TIDYING);
        System.out.println("TERMINATED: " + TERMINATED);
        System.out.println("runStateOf: " + runStateOf(10));
        System.out.println("workerCountOf: " + workerCountOf(10));
        System.out.println("ctlOf: " + ctlOf(RUNNING, 0));

        String time = "2018-11-07 19:51:16";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(df.format(date));

        System.out.println(date.getTime());
    }
}
