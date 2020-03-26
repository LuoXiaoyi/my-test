package com.cpu;

import java.util.*;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-26 17:17
 **/
public class MergeUtil {

    /**
     * 对多个 StackNode 进行合并计算，并且输出合并之后的结果
     * static long[] mids1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
     * static long[] mids2 = {1, 2, 3, 4, 55, 66, 77, 88, 9};
     * 如果对上述两个栈进行合并，合并之后的结果为
     * 1, 2, 3, 4, 5, 6, 7, 8, 9
     *           \-55, 66, 77, 88, 9
     * 如果 mids1 的调用次数为 1，mids2 的调用次数为 2，那么合并之后，1~4 的方法调用次数为 1+3，而 5~9 为 1；55 ~ 9 则为 2
     * 如果计算百分比，那么 5 是 1/(1+2)，而 55 则是 2/(1+2)
     * @param sns 被合并的 StackNode 集合
     * @return 合并之后的结果
     */
    public static List<StackNode> mergeStackNodes(List<StackNode> sns) {
        if (sns != null && !sns.isEmpty()) {
            Map<Long, StackNode> snGroups = new HashMap<>(sns.size());
            for (StackNode sn : sns) {
                long id = sn.getFrames().get(0).getId();
                StackNode target = snGroups.get(id);
                if (target == null) {
                    snGroups.put(id, sn);
                } else {
                    target.addToBeMergedStack(sn);
                }
            }

            Collection<StackNode> mayBeMergedNodes = snGroups.values();
            for (StackNode sn : mayBeMergedNodes) {
                if (sn.shouldBeMerged()) {
                    sn.merge();
                }
            }

            return new ArrayList<>(mayBeMergedNodes);
        }

        return null;
    }
}
