package com.atlxc.VulnScan.utils;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ScansService;
import com.atlxc.VulnScan.product.apiservice.TargetsService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
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

    /** 用来记录当前在线连接数。设计成线程安全的。*/
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static ConcurrentHashMap<String, Session> wsServerMAP = new ConcurrentHashMap<>();
    public static ScansService scanService;
    public static TargetsService targetService;
    public static ScanRecordService scanRecordService;
    public static ConnectorService connectorService;

    /**
     * 连接成功
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("连接成功");
        wsServerMAP.put(session.getId(),session);
        onlineCount.incrementAndGet();
        log.info("客户端连接建立成功，Session ID：{}，当前在线数：{}", session.getId(), onlineCount.get());
    }
    /**
     * 连接关闭
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("连接关闭");
        // 在线数减1
        onlineCount.decrementAndGet();
        // 移除关闭的客户端连接
        wsServerMAP.remove(session.getId());
        log.info("客户端连接关闭成功，Session ID：{}，当前在线数：{}", session.getId(), onlineCount.get());
    }
    /**
     * 接收到消息
     * @param message
     */
    @OnMessage
    public void onMessage(String message, Session session) throws ExecutionException, InterruptedException {
        log.info("服务端接收消息成功，Session ID：{}，消息内容：{}", session.getId(), message);
        JSONObject jsonObject=JSONObject.parseObject(message);

        switch (jsonObject.getString("action")){
            case "getStatus":
                while (true){
                    CompletableFuture<String> getstatus = connectorService.getStatus(jsonObject.getInteger("id"));
                    CompletableFuture.allOf(getstatus).join();
                    String status=getstatus.get();
                    log.info("status:{}",status);
                    if(status!=null&&!status.equals("processing")){
                        this.sendMessage(status, session);
                        break;
                    }
                }
            default:
                break;
        }
    }
    /**
     * 处理消息，并响应给客户端
     * @param message 客户端发送的消息内容
     * @param session 客户端连接对象
     */
    public void sendMessage(String message,Session session){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        String response= jsonObject.toString();
        for(Map.Entry<String,Session>sessionEntry:wsServerMAP.entrySet()){
            Session s=sessionEntry.getValue();
            //过滤自己
            if((session.getId().equals(s.getId()))){
                log.info("服务端发送消息成功，Session ID：{}，消息内容：{}", s.getId(), response);
                s.getAsyncRemote().sendText(response);
            }
        }
    }
    /**
     * 连接异常
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("连接异常：{}", error.toString());
    }
}
