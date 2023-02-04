package com.atlxc.VulnScan.product.dao;

import com.atlxc.VulnScan.product.entity.TargetInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 目标记录表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-04 22:17:24
 */
@Mapper
public interface TargetInfoDao extends BaseMapper<TargetInfoEntity> {
	
}
