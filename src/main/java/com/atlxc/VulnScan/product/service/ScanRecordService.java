package com.atlxc.VulnScan.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;

import java.util.Map;

/**
 * 扫描记录表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-05 14:45:02
 */
public interface ScanRecordService extends IService<ScanRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
    Boolean updateStatus(Integer id, String status);
    String getStatusById(Integer id);
}

