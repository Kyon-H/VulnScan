package com.atlxc.VulnScan.product.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 扫描记录表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-05 14:45:02
 */
@Data
@TableName(value="scan_record",autoResultMap = true)
public class ScanRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
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
	/**
	 * 漏洞等级分布
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
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

}
