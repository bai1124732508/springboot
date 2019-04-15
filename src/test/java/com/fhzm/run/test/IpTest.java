package com.fhzm.run.test;

import com.alibaba.fastjson.JSONObject;
import com.fhzm.common.HttpUtils;
import com.fhzm.entity.generator.IpVo;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class IpTest {

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        RestTemplate restTemplate = new RestTemplate();
        //新浪查询失败查询阿里
        String ip ="162.128.125.6";
        String url = "http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
        String object = restTemplate.getForObject(url, String.class,ip);
        //JSONObject object = get(url);
        System.out.println("object: " + object.toString());
        IpVo ipVo = new Gson().fromJson(object.toString(), IpVo.class);

        // XX表示内网
        if(ipVo.getCode()==0 && !ipVo.getData().getRegion().equals("XX")){
            System.out.println(ipVo.getData().getCountry());
            System.out.println(ipVo.getData().getRegion());
            System.out.println(ipVo.getData().getCity());
        }
        long e = System.currentTimeMillis();
        System.out.println(e-s);
    }


    public static JSONObject get(String url) {
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-type", "application/json; charset=utf-8");
        headers.put("Accept", "application/json");
        JSONObject json =  HttpUtils.doGet(url,headers);
        return json;
    }



}
