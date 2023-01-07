package com.atlxc.VulnScan.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.VulnInfoDao;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.VulnInfoService;


@Service("vulnInfoService")
public class VulnInfoServiceImpl extends ServiceImpl<VulnInfoDao, VulnInfoEntity> implements VulnInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VulnInfoEntity> page = this.page(
                new Query<VulnInfoEntity>().getPage(params),
                new QueryWrapper<VulnInfoEntity>()
        );

        return new PageUtils(page);
    }

}