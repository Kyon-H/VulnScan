package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.SslUtils;
import com.atlxc.VulnScan.vo.AddTargetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
     * 获取所有目标信息
     * Method:GET
     * URL: /api/v1/targets
     * @return
     */
    public JSONObject getTargets() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets";
        //SslUtils.ignoreSsl();
        ResponseEntity<JSONObject> entity=restTemplate.getForEntity(url,JSONObject.class,headers);
        log.info(String.valueOf(entity.getStatusCode()));
        if(entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            throw new RRException("请求错误");
        }

    }

    /**
     * Method:POST
     * URL: /api/v1/targets
     * @param param
     * @return
     */
    public Map<String,Object> addTargets(Map<String,Object> param) {
        log.info(JSONObject.toJSONString(param));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets";
        JSONObject object=new JSONObject();
        object.put("address",param.get("address"));
        object.put("description", param.get("description"));
        HttpEntity<String> entity = new HttpEntity<String>(object.toString(),headers);
        //SslUtils.ignoreSsl();
        ResponseEntity<JSONObject> result = restTemplate.postForEntity(url, entity,JSONObject.class);
        log.info(String.valueOf(result.getStatusCode()));
        Map<String,Object> map=new HashMap<String,Object>();
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
     * Method:DELETE
     * URL: /api/v1/targets/{targetId}
     */
    public void deleteTargets(String targetId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets/"+targetId;
        restTemplate.delete(url,headers);
    }

    /**
     * criticality设置
     * Method:PATCH
     * URL: /api/v1/targets/{targetId}
     */
    public void patchTargets(String targetId,String description,Integer criticality) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets/"+targetId;
        JSONObject object=new JSONObject();
        object.put("description", description);
        object.put("criticality", criticality);
        HttpEntity<String> entity = new HttpEntity<String>(object.toString(),headers);
        JSONObject result = restTemplate.patchForObject(url, entity,JSONObject.class);
        log.info(String.valueOf(result));

    }

    /**
     * 扫描速度
     * Method:PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void pathSpeed(String targetId,String scanSpeed) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets/"+targetId+"/configuration";
        JSONObject object=new JSONObject();
        object.put("scan_speed", scanSpeed);
        HttpEntity<JSONObject> entity=new HttpEntity<JSONObject>(object,headers);
        JSONObject result = restTemplate.patchForObject(url, entity,JSONObject.class);
        log.info(String.valueOf(result));
    }

    /**
     * 网站登录设置
     * Method: PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void patchLogin(String targetId,String credentials) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        String url=ConfigConstant.AWVS_API_URL+"targets/"+targetId+"/configuration";
        JSONObject object=new JSONObject();
        JSONObject kind=new JSONObject();
        if(credentials==null||credentials.equals("")){
            kind.put("kind","none/sequence");
        }else {
            kind.put("kind","automatic");
            kind.put("credentials",JSONObject.parseObject(credentials));
        }
        object.put("login",kind);
        HttpEntity<JSONObject> entity=new HttpEntity<JSONObject>(object,headers);
        JSONObject result = restTemplate.patchForObject(url, entity,JSONObject.class);
        log.info(String.valueOf(result));
    }
}
