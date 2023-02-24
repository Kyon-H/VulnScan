package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.apiservice.VulnService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.DateUtils;
import com.atlxc.VulnScan.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Kyon-H
 * @date 2023/2/17 13:21
 */
@Slf4j
@Service
public class ConnectorService {
    private static final int INTERVAL = 1000;

    @Async("connectorExecutor")
    public void getScanId(ScanRecordEntity entity) {
        TargetService targetService = (TargetService) SpringContextUtils.getBean("targetService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        VulnService vulnService = (VulnService) SpringContextUtils.getBean("vulnService");
        VulnInfoService vulnInfoService = (VulnInfoService) SpringContextUtils.getBean("vulnInfoService");
        //
        String targetId = entity.getTargetId();
        try {
            while (true) {
                Thread.sleep(INTERVAL * 2);
                String tmp = targetService.getScanId(targetId);
                log.info("getScanId:" + tmp);
                if (tmp != null) {
                    String scanId = tmp;
                    ScanRecordEntity tmpEntity = scanService.getStatus(scanId);
                    entity.setScanId(scanId);
                    if (tmpEntity.getStatus() == null || tmpEntity.getSeverityCounts() == null) continue;
                    if (tmpEntity.getStatus().equals("processing")) continue;
                    entity.setStatus(tmpEntity.getStatus());
                    entity.setSeverityCounts(tmpEntity.getSeverityCounts());
                    entity.setScanSessionId(tmpEntity.getScanSessionId());
                    if (!scanRecordService.updateById(entity)) continue;
                    log.info("update success");
                    //获取漏洞信息
                    Map<String, Object> params = new HashMap<>();
                    params.put("target_id", entity.getTargetId());
                    JSONObject jsonObject = vulnService.selectVulns(params);
                    JSONArray vulnInfoArray = jsonObject.getJSONArray("vulnerabilities");
                    Integer scanRecordId = entity.getId();
                    for (int i = 0; i < vulnInfoArray.size(); i++) {
                        JSONObject item = vulnInfoArray.getJSONObject(i);
                        Date lastSeen = DateUtils.stringToDate(item.getString("last_seen"), DateUtils.DATE_TIME_ZONE_PATTERN);
                        VulnInfoEntity vulnInfo = new VulnInfoEntity();
                        vulnInfo.setScanRecordId(scanRecordId);
                        vulnInfo.setVulnId(item.getString("vuln_id"));
                        vulnInfo.setSeverity(item.getInteger("severity"));
                        vulnInfo.setVulnerability(item.getString("vt_name"));
                        vulnInfo.setTargetAddress(item.getString("affects_url"));
                        vulnInfo.setConfidence(item.getInteger("confidence"));
                        vulnInfo.setLastSeen(lastSeen);
                        vulnInfoService.save(vulnInfo);
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async("connectorExecutor")
    public void getStatus(ScanRecordEntity entity) {
        log.info("getStatus entity");
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        String scanId = entity.getScanId();
        try {
            while (true) {
                ScanRecordEntity status = scanService.getStatus(scanId);
                entity.setSeverityCounts(status.getSeverityCounts());
                log.info(entity.getSeverityCounts().toString());
                log.info(entity.getStatus());
                if (!status.getStatus().equals("processing")) {
                    entity.setStatus(status.getStatus());
                    scanRecordService.updateById(entity);
                    break;
                }
                Thread.sleep(INTERVAL);
            }
        } catch (InterruptedException e) {
            log.error("监控线程意外中断{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async("connectorExecutor")
    public CompletableFuture<String> getStatus(Integer id) throws InterruptedException {
        log.info("getStatus id:{}", id);
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        TargetService targetService = (TargetService) SpringContextUtils.getBean("targetService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        try {
            ScanRecordEntity entity = scanRecordService.getById(id);
            if (entity != null && !entity.getStatus().equals("processing")) {
                return CompletableFuture.completedFuture(entity.getStatus());
            }
            while (entity.getScanId() == null) {
                String scanId = targetService.getScanId(entity.getTargetId());
                entity.setScanId(scanId);
            }
            while (true) {
                String scanId = entity.getScanId();
                ScanRecordEntity status = scanService.getStatus(scanId);
                if (status.getStatus() == null) continue;

                entity.setStatus(status.getStatus());
                entity.setSeverityCounts(status.getSeverityCounts());

                log.info(entity.getSeverityCounts().toString());
                log.info(entity.getStatus());
                if (!entity.getStatus().equals("processing")) {
                    scanRecordService.updateById(entity);
                    return CompletableFuture.completedFuture(entity.getStatus());
                }
                Thread.sleep(INTERVAL * 2);
            }
        } catch (Exception e) {
            log.error("监控线程意外中断{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
