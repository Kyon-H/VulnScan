package com.atlxc.VulnScan.product.dao;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 扫描记录表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-05 14:45:02
 */
@Mapper
public interface ScanRecordDao extends BaseMapper<ScanRecordEntity> {
	@Update("update scan_record set status = #{status} where id = #{id}")
    Boolean updateStatus(Integer id, String status);
    @Update("update scan_record set severity_counts = #{severity} where id = #{id}")
    Boolean updateSeverity(Integer id, String severity);
}
