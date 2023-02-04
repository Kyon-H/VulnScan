package com.atlxc.VulnScan.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 目标记录表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-04 22:17:24
 */
@Data
@TableName("target_info")
public class TargetInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Integer userId;
	/**
	 * URL
	 */
	private String address;
	/**
	 * 规范地址
	 */
	private String canonicalAddress;
	/**
	 * 
	 */
	private String domain;
	/**
	 * 危险程度
	 */
	private Integer criticality;
	/**
	 * 
	 */
	private String targetType;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private String targetId;
	/**
	 * 
	 */
	private String type;
	/**
	 * 
	 */
	private String canonicalAddressHash;

}
