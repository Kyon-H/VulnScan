package com.atlxc.VulnScan.product.dao;

import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 扫描报告表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-28 20:39:25
 */
@Mapper
public interface ScanReportDao extends BaseMapper<ScanReportEntity> {
	
}
