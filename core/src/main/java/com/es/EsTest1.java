/*
package com.es;

import com.perfma.esclient.ESClient;
import com.perfma.esclient.ESDao;
import lombok.NonNull;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;

import java.util.Collection;


import java.util.Collection;

*/
/**
 * @author xiaoyiluo
 * @createTime 2018/9/7 23:15
 **//*

public class EsTest1 {

    public static final String esClusterName = "CollectorDBCluster";
    public static final String esClusterNodes = "192.168.0.54:9300";
    public static String RT_PREFIX = "rt_";
    public static String ThreadInfoBeanName = RT_PREFIX + "thread_info";

    public static void main(String[] args) {
        String taskId = "666667";
        Long startTimeStamp = 1536299397087L;
        Long endTimeStamp = 1536299497087L;

        Collection<ThreadInfoBean> beans = queryBetweenTimeStamp(
                ThreadInfoBean.class, taskId, startTimeStamp, endTimeStamp, 0, 9999);

        System.out.println(beans.size());
    }

    public static <T> Collection<T> queryBetweenTimeStamp(@NonNull Class<T> clazz, @NonNull String taskId,
                                                          @NonNull Long startTimeStamp, @NonNull Long endTimeStamp,
                                                          Integer beginIdx, Integer size) {
        SearchRequestBuilder searchRequestBuilder =
                ESDao.getInstance().getSearchRequestBuilder(ThreadInfoBeanName)
                        .addAggregation(AggregationBuilders.max("max_time_stamp").field("time_stamp"))
//                        .setFrom(beginIdx == null ? 0 : beginIdx).setSize(size)
                        .setPostFilter(QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("task_id", taskId))
//                                .must(QueryBuilders.rangeQuery("time_stamp").gte(startTimeStamp).lt(endTimeStamp))
                        );
        Max max_time_stamp = searchRequestBuilder.get().getAggregations().get("max_time_stamp");
        Double max = max_time_stamp.getValue();
        System.out.println("max_time_stamp: " + max.longValue());

        return ESDao.getInstance().search(searchRequestBuilder, clazz);
    }
}
*/
