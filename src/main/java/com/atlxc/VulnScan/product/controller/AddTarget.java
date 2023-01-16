package com.atlxc.VulnScan.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.utils.R;
import com.atlxc.VulnScan.utils.SslUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class AddTarget {



    @RequestMapping("/addTarget")
    public R addTarget(@RequestBody String target){
        log.info("target:{}", target);

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth",ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        JSONObject object=new JSONObject();
        object.put("address",target);
        object.put("description", "url");
        object.put("criticality", "10");
        //HttpEntity<String> entity = new HttpEntity<String>(object.toString(),headers);
        String url = ConfigConstant.AWVS_API_URL+"info";
        String result = template.postForObject(url, null,String.class);
        log.info("result:{}", result);
        return R.ok().put("result", result);
    }
}
