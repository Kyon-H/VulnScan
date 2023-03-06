package com.atlxc.VulnScan.config;

import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import com.atlxc.VulnScan.utils.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类
 *
 * @author Kyon-H
 * @date 2023/2/14 23:09
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setScansService(ScanService scanService) {
        WebSocketServer.scanService = scanService;
    }

    @Autowired
    public void setTargetsService(TargetService targetService) {
        WebSocketServer.targetService = targetService;
    }

    @Autowired
    public void setScanRecordService(ScanRecordService scanRecordService) {
        WebSocketServer.scanRecordService = scanRecordService;
    }

    @Autowired
    public void setConnectorService(ConnectorService connectorService) {
        WebSocketServer.connectorService = connectorService;
    }
}
