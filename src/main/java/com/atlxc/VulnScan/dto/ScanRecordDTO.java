package com.atlxc.VulnScan.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.Date;

/**
 * @author Kyon-H
 * @date 2023/4/24 14:20
 */
@Data
public class ScanRecordDTO {

    private Integer id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 目标ID
     */
    private String targetId;
    /**
     * 扫描ID
     */
    private String scanId;
    /**
     * 扫描会话 id
     */
    private String scanSessionId;
    /**
     * URL
     */
    private String address;
    /**
     * 扫描类型
     */
    private String type;

    @JsonAlias("severity_counts") // 指定 JSON 数据的别名
    private JSONObject severityCounts;
    /**
     * 状态
     */
    private String status;
    /**
     * 扫描时间
     */
    private Date scanTime;
    /**
     * 描述
     */
    private String description;

    private String profileId;

    private String name;
}
