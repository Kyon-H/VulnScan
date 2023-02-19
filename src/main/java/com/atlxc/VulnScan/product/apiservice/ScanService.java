package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
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
public class ScanService {

    /**
     * 添加扫描
     * Method:POST
     * URL: /api/v1/scans
     */
    public Map<String, Object> postScans(ScanRecordEntity scanRecord) {
        log.info("postScans()");
        String url = ConfigConstant.AWVS_API_URL + "scans";
        JSONObject body = new JSONObject();
        JSONObject schedule = new JSONObject();
        schedule.put("disable", false);
        schedule.put("start_date", null);
        schedule.put("time_sensitive", false);
        body.put("target_id", scanRecord.getTargetId());
        body.put("profile_id", scanRecord.getType());
        body.put("schedule", schedule);
        JSONObject responseEntity = new AWVSRequestUtils().POST(url,body);
        log.info(responseEntity.toString());
        if(responseEntity==null)
            throw new RRException("添加扫描失败");
        Map<String, Object> map=JSONObject.toJavaObject(responseEntity,Map.class);
        return map;
    }
    /**
     * 获取单个扫描状态
     * Method:GET
     * URL: /api/v1/scans/{scan_id}
     */
    public ScanRecordEntity getStatus(String scanId) {
        log.info("getStatus(),scanID: {}", scanId);
        String url = ConfigConstant.AWVS_API_URL + "scans/"+scanId;
        JSONObject responseEntity = new AWVSRequestUtils().GET(url);
        if(responseEntity==null)
            throw new RRException("获取扫描状态失败");
        ScanRecordEntity scanRecord=new ScanRecordEntity();
        JSONObject map = responseEntity.getJSONObject("current_session");
        scanRecord.setSeverityCounts(map.getJSONObject("severity_counts"));
        scanRecord.setStatus(map.getString("status"));
        log.info(scanRecord.toString());
        return scanRecord;
    }
    /**
     * 删除扫描
     * Method:DELETE
     * URL: /api/v1/scans/{scan_id}
     */
    public Boolean deleteScans(String scanId) {
        String url = ConfigConstant.AWVS_API_URL + "scans/"+scanId;
        String code = new AWVSRequestUtils().DELETE(url);
        if(!code.equals("024")) throw new RRException("删除扫描失败");
        return true;
    }
}
