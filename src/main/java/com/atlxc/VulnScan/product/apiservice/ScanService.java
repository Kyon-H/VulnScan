package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import org.springframework.stereotype.Service;

/**
 * @author Kyon-H
 * @date 2023/1/28 13:19
 */

@Service
public class ScanService {

    private static final String URL = ConfigConstant.AWVS_API_URL + "scans";

    /**
     * 添加扫描
     * Method:POST
     * URL: /api/v1/scans
     */
    public JSONObject postScans(ScanRecordEntity scanRecord) {
        JSONObject body = new JSONObject();
        JSONObject schedule = new JSONObject();
        schedule.put("disable", false);
        schedule.put("start_date", null);
        schedule.put("time_sensitive", false);
        body.put("target_id", scanRecord.getTargetId());
        body.put("profile_id", scanRecord.getType());
        body.put("schedule", schedule);
        JSONObject response = AWVSRequestUtils.POST(URL, body);
        if (response == null)
            throw new RRException("添加扫描失败");
        return response;
    }

    /**
     * 获取单个扫描状态
     * Method:GET
     * URL: /api/v1/scans/{scan_id}
     */
    public ScanRecordEntity getStatus(String scanId) {
        JSONObject responseEntity = AWVSRequestUtils.GET(URL + "/" + scanId);
        if (responseEntity == null)
            throw new RRException("获取扫描状态失败");
        ScanRecordEntity scanRecord = new ScanRecordEntity();
        JSONObject json = responseEntity.getJSONObject("current_session");
        scanRecord.setSeverityCounts(json.getJSONObject("severity_counts"));
        scanRecord.setScanSessionId(json.getString("scan_session_id"));
        scanRecord.setStatus(json.getString("status"));
        return scanRecord;
    }

    /**
     * 删除扫描
     * Method:DELETE
     * URL: /api/v1/scans/{scan_id}
     */
    public void deleteScans(String scanId) {
        Boolean result = AWVSRequestUtils.DELETE(URL + "/" + scanId);
        if (!result) throw new RRException("删除扫描失败");
    }

    /**
     * 单个扫描概况信息
     * Method:GET
     * URL: /api/v1/scans/{scan_id}/results/{scan_session_id}/statistics
     */
    public JSONObject getStatistics(String scanId, String scanSessionId) {
        JSONObject result = AWVSRequestUtils.GET(URL + "/" + scanId + "/results/" + scanSessionId + "/statistics");
        if (result == null) throw new RRException("获取单个扫描概况信息失败");
        return result;
    }

    /**
     * 单个扫描漏洞结果
     * Method:GET
     * URL: /api/v1/scans/{scan_id}/results/{scan_session_id}/vulnerabilities?l={count}&s=severity:desc
     */
    public JSONObject getVulnerabilities(String scanId, String scanSessionId, String count) {
        String url = URL + "/" + scanId + "/results/" + scanSessionId + "/vulnerabilities?l=" + count + "&s=severity:desc";
        JSONObject result = AWVSRequestUtils.GET(url);
        if (result == null) throw new RRException("获取单个扫描漏洞结果失败");
        return result;
    }

    /**
     * 获取当前扫描单个漏洞信息
     * Method: GET
     * URL: /api/v1/scans/{scan_id}/results/{scan_session_id}/vulnerabilities/{vuln_id}
     */
    public JSONObject getVulnerability(String scanId, String scanSessionId, String vulnId) {
        String url = URL + "/" + scanId + "/results/" + scanSessionId + "/vulnerabilities/" + vulnId;
        JSONObject result = AWVSRequestUtils.GET(url);
        if (result == null) throw new RRException("获取当前扫描单个漏洞失败");
        return result;
    }
}
