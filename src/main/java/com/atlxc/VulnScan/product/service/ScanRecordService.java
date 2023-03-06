package com.atlxc.VulnScan.product.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;

import java.util.List;
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

    Boolean updateSeverity(Integer id, JSONObject severity);

    String getStatusById(Integer id);

    Boolean updateAll(Integer userId);

    ScanRecordEntity getByTargetId(String targetId);
    List<ScanRecordEntity> getByUserId(Integer userId);
    List<ScanRecordEntity> getByUserName(String UserName);

    ScanRecordEntity getById(Integer id, Integer userId);
    Boolean removeByIds(Integer id,List<Integer> vulnIds);
}

