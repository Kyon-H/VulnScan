package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    ScanService scanService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Integer userId=(Integer) params.get("userId");
        if(StringUtils.isNotEmpty((String) params.get("sidx"))) {
            Boolean isAsc=params.get("order").toString().equals("asc")?Boolean.TRUE:Boolean.FALSE;
            //排序
            IPage<ScanRecordEntity> page = this.page(
                    new Query<ScanRecordEntity>().getPage(params,params.get("sidx").toString(),isAsc),
                    new QueryWrapper<ScanRecordEntity>().eq("user_id",userId)
            );
            return new PageUtils(page);
        }else {
            IPage<ScanRecordEntity> page = this.page(
                    new Query<ScanRecordEntity>().getPage(params),
                    new QueryWrapper<ScanRecordEntity>().eq("user_id",userId)
            );
            return new PageUtils(page);
        }
    }
    @Override
    public Boolean updateStatus(Integer id, String status) {

        return baseMapper.updateStatus(id, status);
    }
    @Override
    public Boolean updateSeverity(Integer id, JSONObject severity) {
        return baseMapper.updateSeverity(id, severity.toString());
    }
    @Override
    public String getStatusById(Integer id) {
        ScanRecordEntity entity =baseMapper.selectOne(new QueryWrapper<ScanRecordEntity>().eq("id",id));
        return entity.getStatus();
    }
    @Override
    public Boolean updateAll(Integer userId) {
        List<ScanRecordEntity> entities = baseMapper.selectList(new QueryWrapper<ScanRecordEntity>().eq("user_id", userId));
        entities.forEach(entity -> {
            ScanRecordEntity status = scanService.getStatus(entity.getScanId());
            baseMapper.updateSeverity(entity.getId(),status.getSeverityCounts().toString());
        });
        return true;
    }
}