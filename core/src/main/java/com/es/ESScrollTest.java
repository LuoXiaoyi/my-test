package com.es;

import com.alibaba.fastjson.JSON;
import com.es.beans.HotMethodFrameBean;
import com.myself.TimeCostRecoder;
import com.perfma.esclient.ESDao;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/28 19:35
 **/
public class ESScrollTest {

    public static void main(String[] args) throws UnknownHostException {
        // test();
        test2();
    }

    private static void test2() {

        Client client = ESDao.getInstance().getEsClient().getClient();
        SearchRequestBuilder searchRequestBuilder =
                ESDao.getInstance().getSearchRequestBuilder("hot_method")
                        .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                        .setScroll(new TimeValue(60000)).setSize(2);

        SearchResponse scrollResp = searchRequestBuilder.get();
        String scrollId = scrollResp.getScrollId();
        System.out.println("scroll id:" + scrollId);
        int i = 0;
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                HotMethodFrameBean bean = JSON.parseObject(hit.getSourceAsString(), HotMethodFrameBean.class);
                System.out.println(bean);
                //System.out.println("" + hit.getSourceAsString());
            }
            scrollResp = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000)).execute().actionGet();
            scrollId = scrollResp.getScrollId();
            System.out.println("scroll id:" + scrollId);
            i++;
        }
        while (i < 2);
        //while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

    }

    private static void test() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "CollectorDBCluster")
                .put("client.transport.sniff", "true")
                //.put("index.max_result_window", 1000000)
                .build();
        TimeCostRecoder recoder = new TimeCostRecoder();
        Client client = new PreBuiltTransportClient(settings);
        // 192.168.0.54:9300
        ((PreBuiltTransportClient) client).addTransportAddress(
                new TransportAddress(InetAddress.getByName("192.168.0.54"), 9300));
        SearchResponse scrollResp = client.prepareSearch("hot_method_query")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setFrom(0).setSize(10)//max of 100 hits will be returned for each scroll
                .setScroll(new TimeValue(60000)) //为了使用 scroll，初始搜索请求应该在查询中指定 scroll 参数，告诉 Elasticsearch 需要保持搜索的上下文环境多长时间（滚动时间
                //.setQuery(QueryBuilders.termQuery("task_id", "274585404974301184"))                 // Query 查询条件
                .get();
        //Scroll until no hits are returned
        long a = 0;
        String scrollId = scrollResp.getScrollId();
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
                // System.out.println("" + hit.getSourceAsString());
                a++;
            }

            scrollResp = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000)).execute().actionGet();
        }
        while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

        System.out.println("total cnt: " + a + ", total cost: " + recoder.getTotalCostTime());
    }
}
