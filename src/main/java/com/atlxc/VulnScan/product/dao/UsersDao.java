package com.atlxc.VulnScan.product.dao;

import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
	
}
