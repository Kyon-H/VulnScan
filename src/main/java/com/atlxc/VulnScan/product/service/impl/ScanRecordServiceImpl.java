package com.atlxc.VulnScan.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.ScanRecordDao;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;


@Service("scanRecordService")
public class ScanRecordServiceImpl extends ServiceImpl<ScanRecordDao, ScanRecordEntity> implements ScanRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ScanRecordEntity> page = this.page(
                new Query<ScanRecordEntity>().getPage(params),
                new QueryWrapper<ScanRecordEntity>()
        );

        return new PageUtils(page);
    }

}