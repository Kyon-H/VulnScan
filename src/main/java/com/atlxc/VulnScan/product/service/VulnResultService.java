package com.atlxc.VulnScan.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.product.entity.VulnResultEntity;

import java.util.Map;

/**
 * 漏洞扫描结果表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
public interface VulnResultService extends IService<VulnResultEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

