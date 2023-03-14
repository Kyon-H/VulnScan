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
    private static Integer ASYNC_SCAN_STATUS = 0;

    /**
     * 获取扫描状态
     *
     * @param targetId
     */
    @Async("connectorExecutor")
    public void getScanRecordStatus(String targetId) {
        ASYNC_SCAN_STATUS = 1;
        TargetService targetService = (TargetService) SpringContextUtils.getBean("targetService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        VulnService vulnService = (VulnService) SpringContextUtils.getBean("vulnService");
        VulnInfoService vulnInfoService = (VulnInfoService) SpringContextUtils.getBean("vulnInfoService");
        //
        ScanRecordEntity entity = scanRecordService.getByTargetId(targetId);
        try {
            while (entity.getScanId() == null) {
                Thread.sleep(INTERVAL);
                //获取scanid
                String scanId = targetService.getScanId(targetId);
                entity.setScanId(scanId);
            }
            while (true) {
                Thread.sleep(INTERVAL * 5);
                String scanId = entity.getScanId();
                //通过scanid获取扫描状态
                ScanRecordEntity tmpEntity = scanService.getStatus(scanId);

                if (tmpEntity.getStatus() == null || tmpEntity.getSeverityCounts() == null) continue;
                // 比较SeverityCounts变化
                JSONObject oldSC = entity.getSeverityCounts();
                JSONObject newSC = tmpEntity.getSeverityCounts();
                if (entity.getStatus().equals("completed")) {
                    break;
                }
                // 在processing状态下，severityCount无变化时
                if (
                        oldSC.getInteger("high").equals(newSC.getInteger("high")) &&
                                oldSC.getInteger("medium").equals(newSC.getInteger("medium")) &&
                                oldSC.getInteger("low").equals(newSC.getInteger("low")) &&
                                oldSC.getInteger("info").equals(newSC.getInteger("info")) &&
                                tmpEntity.getStatus().equals("processing")) {
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
                ASYNC_SCAN_STATUS = 0;
                break;
            }
        } catch (InterruptedException e) {
            log.error("getScanRecordStatus 监控线程意外中断{}", e.getMessage());
            ASYNC_SCAN_STATUS = -1;
            throw new RuntimeException(e);
        }
    }

    /**
     * websocket调用，获取扫描状态、漏洞分布、进度%
     *
     * @param id
     * @return
     */
    @Async("connectorExecutor")
    public CompletableFuture<JSONObject> getStatistics(Integer id) {
        log.info("getStatistics id:{}", id);
        ScanService scanService = (ScanService) SpringContextUtils.getBean("scanService");
        TargetService targetService = (TargetService) SpringContextUtils.getBean("targetService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        try {
            Thread.sleep(INTERVAL * 5);
            ScanRecordEntity entity = scanRecordService.getById(id);
            if (entity == null) {
                throw new RuntimeException("记录不存在");
            }
            //获取scanId scanSessionId
            while (entity.getScanId() == null) {
                Thread.sleep(INTERVAL);
                String scanId = targetService.getScanId(entity.getTargetId());
                entity.setScanId(scanId);
            }
            while (entity.getScanSessionId() == null) {
                Thread.sleep(INTERVAL);
                ScanRecordEntity tmpEntity = scanService.getStatus(entity.getScanId());
                entity.setScanSessionId(tmpEntity.getScanSessionId());
            }
            String scanId = entity.getScanId();
            String scanSessionId = entity.getScanSessionId();
            JSONObject statistics = scanService.getStatistics(scanId, scanSessionId);
            String status = statistics.getString("status");
            JSONObject severity_counts = statistics.getJSONObject("severity_counts");
            String progress = statistics.getJSONObject("scanning_app")
                    .getJSONObject("wvs").getJSONObject("main").getString("progress");
            //
            if (!entity.getStatus().equals(status) && ASYNC_SCAN_STATUS != 1) {
                log.error("ScanStatus 状态不一致");
                this.getScanRecordStatus(entity.getTargetId());
            }
            JSONObject result = new JSONObject();
            if (severity_counts.getInteger("high") == null) {
                severity_counts.put("high", 0);
                severity_counts.put("low", 0);
                severity_counts.put("info", 0);
                severity_counts.put("medium", 0);
            }
            result.put("severity_counts", severity_counts);
            result.put("status", status);
            result.put("progress", progress);
            return CompletableFuture.completedFuture(result);
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
    public CompletableFuture<JSONObject> getReportStatus(String ReportId) {
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
                JSONObject result = new JSONObject();
                result.put("status", entity.getStatus());
                result.put("description", entity.getDescription());
                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception e) {
            log.error("getReportStatus 监控线程意外中断{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
