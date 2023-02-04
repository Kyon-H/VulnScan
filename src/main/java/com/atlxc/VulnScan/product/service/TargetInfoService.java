package com.atlxc.VulnScan.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.product.entity.TargetInfoEntity;

import java.util.Map;

/**
 * 目标记录表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-04 22:17:24
 */
public interface TargetInfoService extends IService<TargetInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

