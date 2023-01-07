package com.atlxc.VulnScan.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

/**
 * 扫描记录表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Data
@TableName("scan_record")
public class ScanRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String type;
	/**
	 * 
	 */
	@NotEmpty
	@URL
	private String websiteUrl;
	/**
	 * 
	 */
	private Date scanTime;
	/**
	 * 
	 */
	private String username;

}
