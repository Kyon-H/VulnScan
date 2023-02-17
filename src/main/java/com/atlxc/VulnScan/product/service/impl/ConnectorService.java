package com.atlxc.VulnScan.product.service.impl;

import com.atlxc.VulnScan.product.apiservice.ScansService;
import com.atlxc.VulnScan.product.apiservice.TargetsService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final int INTERVAL = 5000;

    @Autowired
    TargetsService targetsService;
    @Autowired
    ScansService scansService;
    @Autowired
    ScanRecordService scanRecordService;


    @Async("connectorExecutor")
    public void getStatus(ScanRecordEntity entity){
        try {
            String scanId;
            while (true){
                Thread.sleep(500);
                String tmp = targetsService.getScanId(entity.getTargetId());
                if(!(tmp.equals("")||tmp==null)){
                    scanId=tmp;
                    break;
                }
            }
            while (true){
                ScanRecordEntity status = scansService.getStatus(scanId);
                entity.setSeverityCounts(status.getSeverityCounts());
                log.info(entity.getSeverityCounts().toString());
                if(!status.getStatus().equals("processing")){
                    entity.setStatus(status.getStatus());
                    log.info(entity.getStatus());
                    scanRecordService.save(entity);
                    break;
                }
                Thread.sleep(500);
            }
        }catch (InterruptedException e){
            log.error("监控线程意外中断{}",e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Async("connectorExecutor")
    public CompletableFuture<String> getStatus(Integer id) throws InterruptedException {
        try{
            ScanRecordEntity entity=scanRecordService.getById(id);
            if(entity!=null&&!entity.getStatus().equals("processing")){
                return CompletableFuture.completedFuture(entity.getStatus());
            }
            String scanId;
            while (true){
                Thread.sleep(500);
                String tmp = targetsService.getScanId(entity.getTargetId());
                if(!(tmp.equals("")||tmp==null)){
                    scanId=tmp;
                    break;
                }
            }
            while (true){
                ScanRecordEntity status = scansService.getStatus(scanId);
                entity.setSeverityCounts(status.getSeverityCounts());
                log.info(entity.getSeverityCounts().toString());
                if(!status.getStatus().equals("processing")){
                    entity.setStatus(status.getStatus());
                    log.info(entity.getStatus());
                    scanRecordService.save(entity);
                    break;
                }
                Thread.sleep(500);
            }
        }catch (Exception e){
            log.error("监控线程意外中断{}",e.getMessage());
            return CompletableFuture.completedFuture(e.getMessage());
        }
        return null;
    }
}
