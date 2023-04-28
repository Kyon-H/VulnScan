package com.atlxc.VulnScan.product.service;

import com.atlxc.VulnScan.product.entity.TemplateEntity;
import com.atlxc.VulnScan.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-04-28 22:00:36
 */
public interface TemplateService extends IService<TemplateEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean updateTemplates();
}

