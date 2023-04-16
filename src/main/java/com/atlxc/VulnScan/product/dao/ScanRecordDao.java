package com.atlxc.VulnScan.product.dao;

import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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

    @Select("select scan_record.* from scan_record, users " +
            "where scan_record.user_id = users.id and users.username = #{userName}")
    List<ScanRecordEntity> selectIdByUserName(String userName);

    @Select("select * from scan_record where id = #{id} and user_id = #{userId}")
    ScanRecordEntity selectById(Integer id, Integer userId);

    @Select("select count(1) from scan_record where user_id = #{userId}")
    Integer selectCountByUserId(Integer userId);

    @Select("select scan_record.id, scan_record.address, severity_counts -> '$.high' high, severity_counts -> '$.medium' medium " +
            "from scan_record where user_id=#{userId} " +
            "order by (severity_counts -> '$.high' + severity_counts -> '$.medium') desc " +
            "limit #{count}")
    List<Map<String, String>> selectMostTarget(Integer userId, Integer count);
}
