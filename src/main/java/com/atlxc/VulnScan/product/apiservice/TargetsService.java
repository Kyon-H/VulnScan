package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.SslUtils;
import com.atlxc.VulnScan.vo.AddTargetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/1/28 13:19
 */
@Slf4j
@Service
public class TargetsService {

    /**
     * 添加目标
     * Method:POST
     * URL: /api/v1/targets
     * @param param
     * @return
     */
    public Map<String,Object> addTargets(Map<String,Object> param) {
        log.info("addTargets");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        JSONObject object=new JSONObject();
        Map<String,Object> map=new HashMap<String,Object>();
        //请求头
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //URL
        String url=ConfigConstant.AWVS_API_URL+"targets";
        //请求体
        object.put("address",param.get("address"));
        object.put("description", param.get("description"));
        HttpEntity<String> entity = new HttpEntity<String>(object.toString(),headers);
        //send post request
        ResponseEntity<JSONObject> result = restTemplate.postForEntity(url, entity,JSONObject.class);
        log.info(String.valueOf(result.getStatusCode()));
        //处理
        if(result.getStatusCode().is2xxSuccessful()){
            map.put("address",result.getBody().get("address"));
            map.put("description",result.getBody().get("description"));
            map.put("targetId",result.getBody().get("target_id"));
        }else{
            throw new RRException("请求错误");
        }
        return map;
    }

    /**
     * 扫描速度设置
     * Method:PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void setSpeed(String targetId,String scanSpeed) {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets/"+targetId+"/configuration";
        JSONObject object=new JSONObject();
        object.put("scan_speed", scanSpeed);
        HttpEntity<JSONObject> entity=new HttpEntity<JSONObject>(object,headers);
        restTemplate.patchForObject(url, entity,JSONObject.class);
    }

    /**
     * 网站登录设置
     * Method: PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void setLogin(String targetId,Map<String,Object> credentials) {
        if(credentials==null||credentials.equals("")) return;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets/"+targetId+"/configuration";
        JSONObject object=new JSONObject();
        JSONObject kind=new JSONObject();
        JSONObject cre=JSONObject.parseObject(JSON.toJSONString(credentials));

        kind.put("kind","automatic");
        kind.put("credentials",cre);
        object.put("login",kind);

        HttpEntity<JSONObject> entity=new HttpEntity<JSONObject>(object,headers);
        JSONObject result = restTemplate.patchForObject(url, entity,JSONObject.class);
        log.info(String.valueOf(result));
    }
    /**
     * 获取目标的扫描 id
     * Method:GET
     * URL: /api/v1/targets/{target_id}
     */
    public String getScanId(String targetId) {
        log.info("getScanId(), targetID {}", targetId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url = ConfigConstant.AWVS_API_URL + "targets/"+targetId;
        HttpEntity<JSONObject> entity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, JSONObject.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info(responseEntity.getBody().getString("last_scan_id"));
            return responseEntity.getBody().getString("last_scan_id");
        }else{
            throw new RRException("获取扫描id失败");
        }
    }
}
