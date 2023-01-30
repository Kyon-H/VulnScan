package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/1/28 13:19
 */
public class ScansServices {

    /**
     * 添加扫描
     * Method:POST
     * URL: /api/v1/scans
     */
    public JSONObject postScans(Map<String,Object> params){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"scans";
        JSONObject object=new JSONObject(params);
        HttpEntity<JSONObject> entity = new HttpEntity<>(object,headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url,entity,JSONObject.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }else {
            throw new RRException("添加扫描失败");
        }
    }
}
