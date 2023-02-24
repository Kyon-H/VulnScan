package com.atlxc.VulnScan.utils;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kyon-H
 * @date 2023/2/19 20:03
 */
@Slf4j
public class AWVSRequestUtils {
    /**
     * POST request
     */
    public static JSONObject POST(String url, JSONObject body) {
        log.info("POST url: {}", url);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        return null;
    }

    /**
     * GET request
     */
    public static JSONObject GET(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        return null;
    }

    /**
     * PATCH request
     */
    public static Boolean PATCH(String url, JSONObject body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(body, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, entity, JSONObject.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * DELETE request
     * @param url
     */
    public static Boolean DELETE(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, JSONObject.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
