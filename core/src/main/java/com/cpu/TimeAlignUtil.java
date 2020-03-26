package com.cpu;

/**
 * perfma-merge
 *
 * @author xiluo
 * @createTime 2019-05-30 10:14
 **/
public class TimeAlignUtil {

    /**
     * 时间对齐，orgTime 时间按 mesh 进行对齐，并返回对齐结果
     *
     * @param orgTime 原始时间，毫秒
     * @param mesh    对齐粒度，毫秒
     * @return 对齐结果
     */
    public static long align(long orgTime, int mesh) {
        return mesh == 0 ? orgTime : orgTime - (orgTime % mesh);
    }
}
