package com.atlxc.VulnScan.product.service.impl;

import com.atlxc.VulnScan.product.apiservice.ScansService;
import com.atlxc.VulnScan.product.apiservice.TargetsService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
        TargetsService targetsService = (TargetsService) SpringContextUtils.getBean("targetsService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        ScansService scansService = (ScansService) SpringContextUtils.getBean("scansService");
        String targetId = entity.getTargetId();
        try {
            while (true) {
                Thread.sleep(INTERVAL * 2);
                String tmp = targetsService.getScanId(targetId);
                log.info("getScanId:" + tmp);
                if (tmp != null) {
                    String scanId = tmp;
                    ScanRecordEntity tmpEntity = scansService.getStatus(scanId);
                    entity.setScanId(scanId);
                    if (tmpEntity.getStatus() == null||tmpEntity.getSeverityCounts() == null) continue;
                    entity.setStatus(tmpEntity.getStatus());
                    entity.setSeverityCounts(tmpEntity.getSeverityCounts());
                    if (scanRecordService.updateById(entity)) {
                        break;
                    }
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
        ScansService scansService = (ScansService) SpringContextUtils.getBean("scansService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        String scanId = entity.getScanId();
        try {
            while (true) {
                ScanRecordEntity status = scansService.getStatus(scanId);
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
        ScansService scansService = (ScansService) SpringContextUtils.getBean("scansService");
        TargetsService targetsService = (TargetsService) SpringContextUtils.getBean("targetsService");
        ScanRecordService scanRecordService = (ScanRecordService) SpringContextUtils.getBean("scanRecordService");
        try {
            ScanRecordEntity entity = scanRecordService.getById(id);
            if (entity != null && !entity.getStatus().equals("processing")) {
                return CompletableFuture.completedFuture(entity.getStatus());
            }
            while(entity.getScanId() == null) {
                String scanId = targetsService.getScanId(entity.getTargetId());
                entity.setScanId(scanId);
            }
            while (true) {
                String scanId = entity.getScanId();
                ScanRecordEntity status = scansService.getStatus(scanId);
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
