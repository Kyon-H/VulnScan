package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/1/28 13:19
 */
@Slf4j
@Service
public class ScansService {

    /**
     * 添加扫描
     * Method:POST
     * URL: /api/v1/scans
     */
    public Map<String, Object> postScans(ScanRecordEntity scanRecord) {
        log.info("postScans{}", scanRecord);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url = ConfigConstant.AWVS_API_URL + "scans";
        JSONObject object = new JSONObject();
        JSONObject schedule = new JSONObject();
        schedule.put("disable", false);
        schedule.put("start_date", null);
        schedule.put("time_sensitive", false);
        object.put("target_id", scanRecord.getTargetId());
        object.put("profile_id", scanRecord.getType());
        object.put("schedule", schedule);
        HttpEntity<JSONObject> entity = new HttpEntity<>(object, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, entity, JSONObject.class);
        log.info(responseEntity.getBody().toString());
        Map<String, Object> map = new HashMap<String, Object>();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RRException("添加扫描失败");
        }
    }

}
