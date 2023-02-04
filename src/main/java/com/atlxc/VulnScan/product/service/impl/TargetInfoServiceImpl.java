package com.atlxc.VulnScan.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.TargetInfoDao;
import com.atlxc.VulnScan.product.entity.TargetInfoEntity;
import com.atlxc.VulnScan.product.service.TargetInfoService;


@Service("targetInfoService")
public class TargetInfoServiceImpl extends ServiceImpl<TargetInfoDao, TargetInfoEntity> implements TargetInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TargetInfoEntity> page = this.page(
                new Query<TargetInfoEntity>().getPage(params),
                new QueryWrapper<TargetInfoEntity>()
        );

        return new PageUtils(page);
    }

}