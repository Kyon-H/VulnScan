package com.atlxc.VulnScan.product.entity;

import com.atlxc.VulnScan.validator.group.AddGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 扫描报告表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-28 20:39:25
 */
@Data
@TableName("scan_report")
public class ScanReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * user_id
	 */
	private Integer userId;
	/**
	 * 报告id
	 */
	private String reportId;
	/**
	 * html报告下载地址
	 */
	private String htmlUrl;
	/**
	 * pdf下载地址
	 */
	private String pdfUrl;
	/**
	 * 模板id
	 */
	@NotBlank(groups = {AddGroup.class})
	private String templateId;
	/**
	 * 模板名
	 */
	@NotBlank(groups = {AddGroup.class})
	private String templateName;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * id_list中id类型
	 */
	@NotBlank(groups = {AddGroup.class})
	private String listType;
	/**
	 * id : scan_id,vuln_id
	 */
	@NotBlank(groups = {AddGroup.class})
	private String idList;
	/**
	 * 生成报告状态
	 */
	private String status;
	/**
	 * 报告生成时间
	 */
	private Date generationDate;

}
