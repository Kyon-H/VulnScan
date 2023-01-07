package com.atlxc.VulnScan.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 漏洞扫描结果表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Data
@TableName("vuln_result")
public class VulnResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Integer scanRecordId;
	/**
	 * 
	 */
	private Integer vulnerabilityInfoId;
	/**
	 * 
	 */
	private String status;

}
