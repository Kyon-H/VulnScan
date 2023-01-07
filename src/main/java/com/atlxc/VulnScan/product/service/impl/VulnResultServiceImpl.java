package com.atlxc.VulnScan.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.VulnResultDao;
import com.atlxc.VulnScan.product.entity.VulnResultEntity;
import com.atlxc.VulnScan.product.service.VulnResultService;


@Service("vulnResultService")
public class VulnResultServiceImpl extends ServiceImpl<VulnResultDao, VulnResultEntity> implements VulnResultService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VulnResultEntity> page = this.page(
                new Query<VulnResultEntity>().getPage(params),
                new QueryWrapper<VulnResultEntity>()
        );

        return new PageUtils(page);
    }

}