package com.atlxc.VulnScan.product.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;

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

    JSONObject getDetail(Map<String, Object> params);

    List<VulnInfoEntity> getByScanRecordId(Integer scanRecordId);
}

