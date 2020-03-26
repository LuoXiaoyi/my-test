package com.myself;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaoyiluo
 * @createTime 2018/10/15 22:13
 **/
public class DateFormatTest {

    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(df.format(new Date()));
    }
}
