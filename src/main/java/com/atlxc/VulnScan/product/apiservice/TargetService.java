package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/1/28 13:19
 */
@Slf4j
@Service
public class TargetService {

    /**
     * 添加目标
     * Method:POST
     * URL: /api/v1/targets
     *
     * @param param
     * @return
     */
    public Map<String, Object> addTargets(Map<String, Object> param) {
        log.info("addTargets() {}", param);
        //URL
        String url = ConfigConstant.AWVS_API_URL + "targets";
        //请求体
        JSONObject object = new JSONObject();
        object.put("address", param.get("address"));
        object.put("description", param.get("description"));
        //send post request
        JSONObject result = new AWVSRequestUtils().POST(url, object);
        //处理
        if (result == null)
            throw new RRException("添加目标失败");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("address", result.get("address"));
        map.put("description", result.get("description"));
        map.put("targetId", result.get("target_id"));
        return map;
    }

    /**
     * 扫描速度设置
     * Method:PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void setSpeed(String targetId, String scanSpeed) {
        String url = ConfigConstant.AWVS_API_URL + "targets/" + targetId + "/configuration";
        JSONObject object = new JSONObject();
        object.put("scan_speed", scanSpeed);
        new AWVSRequestUtils().PATCH(url, object);
    }

    /**
     * 网站登录设置
     * Method: PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void setLogin(String targetId, Map<String, Object> credentials) {
        if (credentials == null || credentials.equals("")) return;
        //url
        String url = ConfigConstant.AWVS_API_URL + "targets/" + targetId + "/configuration";
        //body
        JSONObject object = new JSONObject();
        JSONObject kind = new JSONObject();
        JSONObject cre = JSONObject.parseObject(JSON.toJSONString(credentials));
        kind.put("kind", "automatic");
        kind.put("credentials", cre);
        object.put("login", kind);
        new AWVSRequestUtils().PATCH(url, object);
    }

    /**
     * 获取目标的扫描 id
     * Method:GET
     * URL: /api/v1/targets/{target_id}
     */
    public String getScanId(String targetId) {
        log.info("getScanId(), targetID {}", targetId);
        //url
        String url = ConfigConstant.AWVS_API_URL + "targets/" + targetId;
        //request
        JSONObject responseEntity = new AWVSRequestUtils().GET(url);
        if (responseEntity == null) {
            throw new RRException("获取扫描id失败");
        }
        log.info(responseEntity.getString("last_scan_id"));
        return responseEntity.getString("last_scan_id");
    }
}
