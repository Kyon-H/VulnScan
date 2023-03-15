package com.atlxc.VulnScan.product.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 漏洞信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
public interface VulnInfoService extends IService<VulnInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean updateAll();

    JSONObject getDetail(Integer vulnInfoId, Integer userId);

    List<VulnInfoEntity> getByScanRecordId(Integer scanRecordId);

    VulnInfoEntity getByVulnId(String vulnId);

    JSONArray getSeverityCount(Integer userId);
}

