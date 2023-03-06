package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/2/19 23:36
 */
@Slf4j
@Service
public class VulnService {
    private static final String URL = ConfigConstant.AWVS_API_URL + "vulnerabilities";

    /**
     * 获取所有漏洞信息
     * Method:GET
     * URL: /api/v1/vulnerabilities?l=20&q=status:open
     */
    public JSONObject getAllVulns(Integer l, String q) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL).append("?");
        if (l != null) {
            sb.append("l=").append(l).append("&");
        }
        if (q != null) {
            sb.append("q=status:").append(q).append("&");
        }
        JSONObject result = AWVSRequestUtils.GET(sb.toString());
        if (result == null) throw new RRException("获取漏洞信息失败");
        return result;
    }

    /**
     * 条件筛选漏洞信息
     * Method:GET
     * URL: /api/v1/vulnerabilities?q=severity:{int};status:{string};target_id:{target_id};confidence:{confidence};
     */
    public JSONObject selectVulns(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder().append(URL + "?q=");
        if (params.get("severity") != null) {
            sb.append("severity:").append(params.get("severity").toString()).append(";");
        }
        if (params.get("status") != null) {
            sb.append("status:").append(params.get("status").toString()).append(";");
        }
        if (params.get("target_id") != null) {
            sb.append("target_id:").append(params.get("target_id").toString()).append(";");
        }
        if (params.get("confidence") != null) {
            sb.append("confidence:").append(params.get("confidence").toString()).append(";");
        }
        String url = sb.toString();
        JSONObject result = AWVSRequestUtils.GET(url);
        if (result == null) throw new RRException("筛选漏洞信息失败");
        return result;
    }

    /**
     * 获取单个漏洞信息
     * Method:GET
     * URL: api/v1/vulnerabilities/{vuln_id}
     */
    public JSONObject getVuln(String vuln_id) {
        JSONObject result = AWVSRequestUtils.GET(URL + "/" + vuln_id);
        if (result == null) throw new RRException("获取单个漏洞信息失败");
        return result;
    }
}
