package com.myself;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/30 22:27
 **/
public class MapTest {

    public static void main(String[] args) {

        System.out.println(Double.MIN_NORMAL < 0.00000001);

        System.out.println(Double.MIN_VALUE < 0.00000001);


        System.out.println(Double.MIN_VALUE > 0);

        System.out.println(Double.MIN_NORMAL > 0);

        double a = 0.00000001, b = 0.00000001;
        if(a == b){

        }

        JSONObject jo = new JSONObject();
        jo.put("double", 0.00000001);

        JSONObject jo2 = JSONObject.parseObject(jo.toJSONString());
        if(jo2.getDouble("double") == 0.00000001){
            System.out.println("OK");
        }
    }
}
