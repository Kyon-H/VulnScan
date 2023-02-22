package com.atlxc.VulnScan.product.dao;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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
    @Select("select * from scan_record where user_id = #{userId}")
    List<ScanRecordEntity> selectByUserId(Integer userId);
    @Select("select scan_record.* from scan_record, users where scan_record.user_id = users.id and users.username = #{userName}")
    List<ScanRecordEntity> selectIdByUserName(String userName);
}
