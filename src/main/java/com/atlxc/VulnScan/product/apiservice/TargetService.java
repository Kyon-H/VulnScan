package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/1/28 13:19
 */
@Slf4j
@Service
public class TargetService {

    private static final String URL = ConfigConstant.AWVS_API_URL + "targets";

    /**
     * 添加目标
     * Method:POST
     * URL: /api/v1/targets
     *
     * @param param
     * @return
     */
    public JSONObject addTargets(Map<String, Object> param) {
        log.info("addTargets() {}", param);
        //请求体
        JSONObject body = new JSONObject();
        body.put("address", param.get("address"));
        body.put("description", param.get("description"));
        //send post request
        JSONObject result = AWVSRequestUtils.POST(URL, body);
        //处理
        if (result == null)
            throw new RRException("添加目标失败");
        return result;
    }

    /**
     * 扫描速度设置
     * Method:PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void setSpeed(String targetId, String scanSpeed) {
        JSONObject object = new JSONObject();
        object.put("scan_speed", scanSpeed);
        Boolean result = AWVSRequestUtils.PATCH(URL + "/" + targetId + "/configuration", object);
        if (!result) throw new RRException("设置扫描速度失败");
    }

    /**
     * 网站登录设置
     * Method: PATCH
     * URL: /api/v1/targets/{target_id}/configuration
     */
    public void setLogin(String targetId, Map<String, Object> credentials) {
        if (credentials == null || credentials.equals("")) return;
        //body
        JSONObject object = new JSONObject();
        JSONObject kind = new JSONObject();
        JSONObject cre = JSONObject.parseObject(JSON.toJSONString(credentials));
        kind.put("kind", "automatic");
        kind.put("credentials", cre);
        object.put("login", kind);
        Boolean result = AWVSRequestUtils.PATCH(URL + "/" + targetId + "/configuration", object);
        if (!result) throw new RRException("设置网站登录失败");
    }

    /**
     * 获取目标的扫描 id
     * Method:GET
     * URL: /api/v1/targets/{target_id}
     */
    public String getScanId(String targetId) {
        log.info("getScanId(), targetID {}", targetId);
        //request
        JSONObject responseEntity = AWVSRequestUtils.GET(URL + "/" + targetId);
        if (responseEntity == null) {
            throw new RRException("获取扫描id失败");
        }
        log.info(responseEntity.getString("last_scan_id"));
        return responseEntity.getString("last_scan_id");
    }

    /**
     * 删除目标
     * Method:DELETE
     * URL: /api/v1/targets/{target_id}
     */
    public void deleteTarget(String targetId) {
        Boolean result = AWVSRequestUtils.DELETE(URL + "/" + targetId);
        if (!result) throw new RRException("删除目标失败");
    }
}
