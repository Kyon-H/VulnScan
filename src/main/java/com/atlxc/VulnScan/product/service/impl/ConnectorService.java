package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ReportService;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.apiservice.VulnService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.ScanReportService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.DateUtils;
import com.atlxc.VulnScan.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    /**
     * 获取扫描状态
     *
     * @param targetId
     */
    @Async("connectorExecutor")
    public void getScanRecordStatus(String targetId) {
        TargetService targetService = (TargetService) SpringContextUtils.getBean("targetService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        VulnService vulnService = (VulnService) SpringContextUtils.getBean("vulnService");
        VulnInfoService vulnInfoService = (VulnInfoService) SpringContextUtils.getBean("vulnInfoService");
        //
        ScanRecordEntity entity = scanRecordService.getByTargetId(targetId);
        try {
            while (true) {
                Thread.sleep(INTERVAL * 10);
                //获取scanid
                String tmp = targetService.getScanId(targetId);
                if (tmp == null) continue;
                String scanId = tmp;
                //通过scanid获取扫描状态
                ScanRecordEntity tmpEntity = scanService.getStatus(scanId);
                entity.setScanId(scanId);
                if (tmpEntity.getStatus() == null || tmpEntity.getSeverityCounts() == null) continue;
                // 比较SeverityCounts变化
                String oldSeverityCount=entity.getSeverityCounts().toString();
                String newSeverityCount=tmpEntity.getSeverityCounts().toString();
                if(oldSeverityCount.equals(newSeverityCount)&&tmpEntity.getStatus().equals("completed")){
                    break;
                }
                // 在processing状态下，severityCount无变化时
                if(oldSeverityCount.equals(newSeverityCount)&&tmpEntity.getStatus().equals("processing")){
                    continue;
                }
                //severityCount 变化时，更新severityCount和status
                //保存status severity
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
                    //避免重复保存
                    if (vulnInfoService.getByVulnId(item.getString("vuln_id")) != null) continue;
                    //获取数据并保存到数据库
                    Date date = DateUtils.stringToDate(item.getString("last_seen"), DateUtils.DATE_TIME_ZONE_PATTERN);
                    Date lastSeen = DateUtils.addDateHours(date, 8);
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
                if (!entity.getStatus().equals("completed")) continue;
                break;
            }
        } catch (InterruptedException e) {
            log.error("getScanRecordStatus 监控线程意外中断{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * websocket调用，获取扫描状态
     *
     * @param id ScanRecordId
     * @return 扫描状态和漏洞分布
     */
    @Async("connectorExecutor")
    public CompletableFuture<JSONObject> getScanStatus(Integer id) {
        log.info("getScanStatus id:{}", id);
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        TargetService targetService = (TargetService) SpringContextUtils.getBean("targetService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        try {
            ScanRecordEntity entity = scanRecordService.getById(id);
            if (entity == null) {
                throw new RuntimeException("记录不存在");
            }
            //获取scanId
            while (entity.getScanId() == null) {
                Thread.sleep(INTERVAL * 2);
                String scanId = targetService.getScanId(entity.getTargetId());
                entity.setScanId(scanId);
            }
            while (true) {
                Thread.sleep(INTERVAL * 6);
                String scanId = entity.getScanId();
                ScanRecordEntity status = scanService.getStatus(scanId);
                //TODO
                if(!entity.getStatus().equals(status.getStatus())) {
                    log.error("ScanStatus 状态不一致");
                    this.getScanRecordStatus(entity.getTargetId());
                }
                JSONObject result = new JSONObject();
                result.put("severity_counts", entity.getSeverityCounts());
                result.put("status", entity.getStatus());
                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception e) {
            log.error("getScanStatus 监控线程意外中断{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取生成报告状态
     *
     * @param ReportId
     * @return 状态信息
     */
    @Async("connectorExecutor")
    public CompletableFuture<String> getReportStatus(String ReportId) {
        log.info("getReportStatus");
        ScanReportService scanReportService = (ScanReportService) SpringContextUtils.getBean("scanReportService");
        ReportService reportService = (ReportService) SpringContextUtils.getBean("reportService");
        //
        ScanReportEntity entity = scanReportService.getByReportId(ReportId);
        try {
            while (true) {
                Thread.sleep(INTERVAL * 2);
                JSONObject report = reportService.getReport(entity.getReportId());
                JSONArray download = report.getJSONArray("download");
                log.info("status:processing");
                if (!report.getString("status").equals("completed")) continue;
                entity.setStatus(report.getString("status"));
                log.info("status:{}", entity.getStatus());
                entity.setDescription(report.getJSONObject("source").getString("description"));
                entity.setHtmlUrl(download.getString(0).replace("/api/v1/reports/download/", ""));
                entity.setPdfUrl(download.getString(1).replace("/api/v1/reports/download/", ""));
                if (!scanReportService.updateById(entity)) continue;
                return CompletableFuture.completedFuture(entity.getStatus());
            }
        } catch (Exception e) {
            log.error("getReportStatus 监控线程意外中断{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
