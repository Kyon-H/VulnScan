package com.atlxc.VulnScan.product.service;

import com.atlxc.VulnScan.product.entity.ScanTypeEntity;
import com.atlxc.VulnScan.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/4/22 16:35
 */
public interface ScanTypeService extends IService<ScanTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean updateScanType();

    List<ScanTypeEntity> getScanTypes();
}
