package com.atlxc.VulnScan.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.ScanReportDao;
import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.product.service.ScanReportService;

@Slf4j
@Service("scanReportService")
public class ScanReportServiceImpl extends ServiceImpl<ScanReportDao, ScanReportEntity> implements ScanReportService {

    @Override
    public PageUtils queryPage(@NotNull Map<String, Object> params) {
        Integer userId= (Integer) params.get("userId");
        if(StringUtils.isNotEmpty((String) params.get("sidx"))) {
            Boolean isAsc=params.get("order").toString().equals("asc")?Boolean.TRUE:Boolean.FALSE;
            IPage<ScanReportEntity> page = this.page(
                    new Query<ScanReportEntity>().getPage(params,params.get("sidx").toString(),isAsc),
                    new QueryWrapper<ScanReportEntity>().eq("user_id",userId)
            );
            return new PageUtils(page);
        }else {
            IPage<ScanReportEntity> page = this.page(
                    new Query<ScanReportEntity>().getPage(params),
                    new QueryWrapper<ScanReportEntity>().eq("user_id",userId)
            );
            return new PageUtils(page);
        }
    }

    @Override
    public ScanReportEntity getByReportId(String reportId) {
        return baseMapper.selectOne(new QueryWrapper<ScanReportEntity>().eq("report_id", reportId));
    }

}