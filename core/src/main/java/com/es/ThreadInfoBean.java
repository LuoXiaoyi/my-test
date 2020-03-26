/*
package com.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

*/
/**
 * @author xiaoyiluo
 * @createTime 2018/8/20 16:27
 **//*

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThreadInfoBean{
    private String taskId;
    private Long timeStamp;

    */
/**
     * 1 新增的、0 现有的、-1 删除的
     *//*

    private Integer type;

    private Long tid;
    private Long pid; // os 上的线程 id，即 LWT 线程 id
    private String threadName;
    private String status;
    private String osThreadState;

    private Integer voluntaryCtxtSwitches;
    private Integer nonvoluntaryCtxtSwitches;

    private String isDeadLock; // 0/1
    private Long userCpuUsage;
    private Long totalCpuUsage;
    private Long allocatedBytes;
    private String stackInfo;
}
*/
