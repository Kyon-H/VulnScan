package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.VulnService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.VulnInfoDao;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.VulnInfoService;

@Slf4j
@Service("vulnInfoService")
public class VulnInfoServiceImpl extends ServiceImpl<VulnInfoDao, VulnInfoEntity> implements VulnInfoService {
    @Autowired
    VulnService vulnService;
    @Autowired
    ScanRecordService scanRecordService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String userName = params.get("userName").toString();
        List<ScanRecordEntity> scanRecord = scanRecordService.getByUserName(userName);
        List<Integer> scanRecordIds = new ArrayList<>();
        scanRecord.forEach(scanRecordEntity -> {
            Integer scanRecordId = scanRecordEntity.getId();
            scanRecordIds.add(scanRecordId);
        });
        QueryWrapper<VulnInfoEntity> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty((String)params.get("scan_record_id"))){
            queryWrapper.eq("scan_record_id",params.get("scan_record_id").toString());
        }
        if(StringUtils.isNotEmpty((String)params.get("severity"))){
            queryWrapper.eq("severity",params.get("severity").toString());
        }
        if(StringUtils.isNotEmpty((String) params.get("sidx"))){
            Boolean isAsc=params.get("order").toString().equals("asc")?Boolean.TRUE:Boolean.FALSE;
            //排序
            queryWrapper.in("scan_record_id",scanRecordIds);
            IPage<VulnInfoEntity> page = this.page(
                    new Query<VulnInfoEntity>().getPage(params,params.get("sidx").toString(),isAsc),
                    queryWrapper
            );
            return new PageUtils(page);
        }else{
            IPage<VulnInfoEntity> page = this.page(
                    new Query<VulnInfoEntity>().getPage(params),
                    queryWrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public Boolean updateAll() {
        log.info("updateAll");
        JSONArray jsonArray = vulnService.getAllVulns(null, "open").getJSONArray("vulnerabilities");
        log.info("vulnerabilities 个数:" + jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String targetId = item.getString("target_id");
            ScanRecordEntity scanRecord = scanRecordService.getByTargetId(targetId);
            if (scanRecord != null) {
                VulnInfoEntity vulnInfo;
                vulnInfo = baseMapper.selectOne(new QueryWrapper<VulnInfoEntity>().eq("vuln_id", item.getString("vuln_id")));
                if (vulnInfo == null) {
                    vulnInfo = new VulnInfoEntity();
                    vulnInfo.setScanRecordId(scanRecord.getId());
                    vulnInfo.setVulnId(item.getString("vuln_id"));
                    vulnInfo.setSeverity(item.getInteger("severity"));
                    vulnInfo.setVulnerability(item.getString("vt_name"));
                    vulnInfo.setTargetAddress(item.getString("affects_url"));
                    vulnInfo.setConfidence(item.getInteger("confidence"));
                    Date last_seen = DateUtils.stringToDate(item.getString("last_seen"), DateUtils.DATE_TIME_ZONE_PATTERN);
                    vulnInfo.setLastSeen(last_seen);
                    baseMapper.insert(vulnInfo);
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public JSONObject getDetail(@NotNull Map<String, Object> params) {
        String userName = params.get("userName").toString();
        Integer vulnInfoId= Integer.parseInt(params.get("vulninfo_id").toString());
        List<ScanRecordEntity> scanRecordEntities = scanRecordService.getByUserName(userName);
        List<Integer> scanRecordIds = new ArrayList<>();
        scanRecordEntities.forEach(scanRecordEntity -> {
            scanRecordIds.add(scanRecordEntity.getId());
        });
        VulnInfoEntity vulnInfoEntity=baseMapper.selectOne(
                new QueryWrapper<VulnInfoEntity>()
                        .in("scan_record_id",scanRecordIds)
                        .eq("id",vulnInfoId)
        );
        if(vulnInfoEntity==null){return null;}
        String vulnId=vulnInfoEntity.getVulnId();
        JSONObject detail = vulnService.getVuln(vulnId);
        return detail;
    }

    @Override
    public List<VulnInfoEntity> getByScanRecordId(Integer scanRecordId) {
        return baseMapper.selectList(new QueryWrapper<VulnInfoEntity>().eq("scan_record_id",scanRecordId));
    }

}