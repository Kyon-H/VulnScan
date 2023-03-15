package com.atlxc.VulnScan.product.dao;

import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 漏洞信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Mapper
public interface VulnInfoDao extends BaseMapper<VulnInfoEntity> {
    @Select("select vuln_info.* from scan_record vuln_info where scan_record_id = #{scanRecordId}")
    List<VulnInfoEntity> selectByTargetId(Integer scanRecordId);

    @Select("select vuln_info.* from vuln_info " +
            "join scan_record on vuln_info.scan_record_id = scan_record.id " +
            "where scan_record.user_id = #{userId} and vuln_info.id=#{vulnInfoId}")
    VulnInfoEntity selectOneByIds(Integer userId, Integer vulnInfoId);

    @Select("select vuln_info.severity, count(1) number from vuln_info " +
            "join scan_record on vuln_info.scan_record_id = scan_record.id " +
            "where scan_record.user_id = #{userId} group by vuln_info.severity")
    List<Map<String, Integer>> selectCountByUserId(Integer userId);
}
