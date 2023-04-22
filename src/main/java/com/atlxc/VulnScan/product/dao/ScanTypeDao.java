package com.atlxc.VulnScan.product.dao;

import com.atlxc.VulnScan.product.entity.ScanTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Kyon-H
 * @date 2023/4/22 16:21
 */
public interface ScanTypeDao extends BaseMapper<ScanTypeEntity> {

    @Select("select * from scan_type")
    List<ScanTypeEntity> selectAll();
}
