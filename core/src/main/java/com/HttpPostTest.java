package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-22 16:57
 **/
public class HttpPostTest {

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            System.out.println("abc");
            Thread.sleep(1000);
        }
        //sendFileUploadFinishedNotify();
        //send();
    }

    public static void send() {
        String url = "http://nginx.dev.perfma-inc.net:17933/sendAppendFinish";
        MqMessage msg = new MqMessage();
        msg.setBiz("CPU");
        msg.setFileKey("group1/M00/08/98/wKgAQlzlC8aAZCJ1AAAAAAAAAAA.rt-cpu");
        msg.setFileLength(123344);
        msg.setFileType("cpu");
        msg.setTaskId(1234556566L);
        msg.setTimestamp(159310201290L);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000).build();

        String ret = null;
        try {
            ret = doPostJson(url, JSON.toJSONString(msg), requestConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ret);
    }

    public static boolean sendFileUploadFinishedNotify() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000).build();

        //String url = "http://midware.dev.perfma-inc.net:17921/sendAppendFinish";
        String url = "http://nginx.dev.perfma-inc.net:17933/sendAppendFinish";
        Map<String, String> paras = new HashMap<>(8);
        paras.put("taskId", "1233");
        paras.put("timestamp", "456");
        paras.put("fileType", "cpu");
        paras.put("fileLength", "1000");
        paras.put("fileKey", "group1/M00/08/98/wKgAQlzlC8aAZCJ1AAAAAAAAAAA.rt-cpu");
        paras.put("biz", "CPU");

        logger.info("request url: {}.", url);
        /*try {
            // postJSON(url, paras);
            String json = doPost(url, paras, requestConfig);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String json = postJSON(url, paras);
        ResultInfo<Object> result = JSON.parseObject(json, new TypeReference<ResultInfo<Object>>() {
        });
        if (result == null) {
            logger.error("call file gateway to send mq msg error, url: {}, paras: {}.", url, paras);
            return false;
        }

        if (!result.isSuccess()) {
            logger.error("send file upload finished notify error: {}", result.getMessage());
        }

        return result.isSuccess();
    }

    public static String postJSON(String url, Map<String, String> paras) {
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            HttpPost hp = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
                    .setSocketTimeout(3000).build();

            hp.setConfig(requestConfig);

            //装填参数
            List<NameValuePair> nvps = new ArrayList<>();
            if (paras != null) {
                for (Map.Entry<String, String> entry : paras.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            //设置参数到请求对象中
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, "utf-8");
            entity.setContentType("application/json");
            hp.setEntity(entity);

            hp.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(hp);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                return EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                logger.error("post request error, url: {}, paras: {}, status code: {}, cause: {}",
                        url, paras, response.getStatusLine().getStatusCode(),
                        EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (Exception e) {
            logger.error("connect to [{}] fail.cause:", url, e);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("client close [{}] failed.", url, e);
                }
            }
        }

        return null;
    }

    public static String doPostJson(String url, String json, RequestConfig config) throws Exception {
        String res = "";
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        try {
            if (config != null) {
                httpPost.setConfig(config);
            }
            // 设置参数到请求对象中
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            // 执行http请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            // 获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                res = EntityUtils.toString(entity, "utf-8");
            }

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                return res;
            } else {
                logger.error("post request error, url: {}, paras: {}, status code: {}, cause: {}",
                        url, json, response.getStatusLine().getStatusCode(), res);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static String doPost(String url, Map<String, String> param, RequestConfig config, Header... headers) throws Exception {
        String res = "";
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Http Post请求对象
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (Header h : headers) {
                httpPost.addHeader(h);
            }
        }
        try {
            if (config != null) {
                httpPost.setConfig(config);
            }
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 创建请求内容
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                // 设置参数到请求对象中
                httpPost.setEntity(entity);
            }
            httpPost.setHeader("Content-type", "application/json");
            // 执行http请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            res = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return res;
    }


    private static Logger logger = LoggerFactory.getLogger(HttpPostTest.class);
}
