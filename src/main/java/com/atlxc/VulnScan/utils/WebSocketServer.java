package com.atlxc.VulnScan.utils;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kyon-H
 * @date 2023/2/14 23:16
 */
@Slf4j
@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    /**
     * 用来记录当前在线连接数。设计成线程安全的。
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static ConcurrentHashMap<String, Session> wsServerMAP = new ConcurrentHashMap<>();
    public static ScanService scanService;
    public static TargetService targetService;
    public static ScanRecordService scanRecordService;
    public static ConnectorService connectorService;
    public static Boolean HeartCheck =false;

    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("连接成功");
        wsServerMAP.put(session.getId(), session);
        onlineCount.incrementAndGet();
        log.info("客户端连接建立成功，Session ID：{}，当前在线数：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭
     *
     * @param session
     */
    @OnClose
    public void onClose(@NotNull Session session) {
        log.info("连接关闭");
        // 在线数减1
        onlineCount.decrementAndGet();
        // 移除关闭的客户端连接
        wsServerMAP.remove(session.getId());
        log.info("客户端连接关闭成功，Session ID：{}，当前在线数：{}", session.getId(), onlineCount.get());
    }

    /**
     * 接收到消息
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, @NotNull Session session) throws ExecutionException, InterruptedException {
        log.info("接收消息，Session ID：{}，消息内容：{}", session.getId(), message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        switch (jsonObject.getString("action")) {
            case "getRecordStatus":
                HeartCheck=true;
                JSONObject status;
                do {
                    CompletableFuture<JSONObject> getstatus = connectorService.getStatistics(jsonObject.getInteger("id"));
                    CompletableFuture.allOf(getstatus).join();
                    status = getstatus.get();
                    status.put("id", jsonObject.getInteger("id"));
                    this.sendMessage(status, session);
                }while (!status.getString("status").equals("completed"));
                HeartCheck=false;
                break;
            case "getReportStatus":
                HeartCheck=true;
                CompletableFuture<JSONObject> reportStatus = connectorService.getReportStatus(jsonObject.getString("reportId"));
                CompletableFuture.allOf(reportStatus).join();
                JSONObject report=reportStatus.get();
                report.put("id", jsonObject.getInteger("id"));
                this.sendMessage(report, session);
                HeartCheck=false;
                break;
            case "HeartCheck":
                JSONObject heartCheck = new JSONObject();
                heartCheck.put("HeartCheck", HeartCheck);
                this.sendMessage(heartCheck,session);
                break;
            default:
                break;
        }
    }

    /**
     * 处理消息，并响应给客户端
     *
     * @param message 客户端发送的消息内容
     * @param session 客户端连接对象
     */
    public void sendMessage(JSONObject message, Session session) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        String response = jsonObject.toString();
        for (Map.Entry<String, Session> sessionEntry : wsServerMAP.entrySet()) {
            Session s = sessionEntry.getValue();
            if ((session.getId().equals(s.getId()))) {
                log.info("发送消息，Session ID：{}，消息内容：{}", s.getId(), response);
                s.getAsyncRemote().sendText(response);
            }
        }
    }

    /**
     * 连接异常
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, @NotNull Throwable error) {
        log.error("连接异常：{}", error.toString());
    }
}
