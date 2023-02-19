package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Kyon-H
 * @date 2023/2/19 23:36
 */
@Slf4j
@Service
public class VulnService {
    private static final String URL=ConfigConstant.AWVS_API_URL+"vulnerabilities";
    /**
     * 获取所有漏洞信息
     * Method:GET
     * URL: /api/v1/vulnerabilities?l=20&q=status:open
     */
    public JSONObject getAllVulns(String l, String q) {
        JSONObject result = new AWVSRequestUtils().GET(URL + "?l=" + l + "&q=status:" + q);
        if(result==null) throw new RRException("获取漏洞信息失败");
        return result;
    }
    /**
     * 条件筛选漏洞信息
     * Method:GET
     * URL: /api/v1/vulnerabilities?q=severity:{int};criticality:{int};status:{string};cvss_score:{logic expression};target_id:{target_id};group_id:{group_id}
     */
    public JSONObject selectVulns(String q, int severity, int criticality, String status, String cv,String target_id,String group_id) {
        String url=URL+"?q=severity:"+severity+";criticality:"+criticality+";status:"+status+";cvss_score:"+cv+";target_id:"+target_id+";group_id:"+group_id;
        JSONObject result = new AWVSRequestUtils().GET(url);
        if(result==null) throw new RRException("筛选漏洞信息失败");
        return result;
    }
    /**
     * 获取单个漏洞信息
     * Method:GET
     * URL: api/v1/vulnerabilities/{vuln_id}
     */
    public JSONObject getVuln(String vuln_id) {
        JSONObject result = new AWVSRequestUtils().GET(URL+"/"+vuln_id);
        if(result==null) throw new RRException("获取单个漏洞信息失败");
        return result;
    }
}
