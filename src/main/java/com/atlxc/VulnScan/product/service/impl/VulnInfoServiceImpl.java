package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.VulnService;
import com.atlxc.VulnScan.product.dao.VulnInfoDao;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.DateUtils;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("vulnInfoService")
public class VulnInfoServiceImpl extends ServiceImpl<VulnInfoDao, VulnInfoEntity> implements VulnInfoService {
    @Autowired
    VulnService vulnService;
    @Autowired
    ScanRecordService scanRecordService;

    @Override
    public PageUtils queryPage(@NotNull Map<String, Object> params) {
        Integer userId =(Integer) params.get("userId");

        MPJQueryWrapper<VulnInfoEntity> queryWrapper = new MPJQueryWrapper<>();
        queryWrapper.selectAll(VulnInfoEntity.class)
                .leftJoin("scan_record on t.scan_record_id = scan_record.id")
                .eq("scan_record.user_id",userId);
        if (params.get("scanRecordId")!= null) {
            queryWrapper.eq("t.scan_record_id", params.get("scanRecordId").toString());
        }
        if (params.get("severity")!=null) {
            queryWrapper.eq("severity", params.get("severity").toString());
        }
        Boolean isAsc= params.get("isAsc") == null || params.get("order").toString().equals("asc");
        String sidx=params.get("sidx")==null?"":params.get("sidx").toString();
        //排序
        IPage<VulnInfoEntity> page = this.page(
                new Query<VulnInfoEntity>().getPage(params, sidx, isAsc),
                queryWrapper
        );
        return new PageUtils(page);
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
        Integer vulnInfoId = Integer.parseInt(params.get("vulninfo_id").toString());
        List<ScanRecordEntity> scanRecordEntities = scanRecordService.getByUserName(userName);
        List<Integer> scanRecordIds = new ArrayList<>();
        scanRecordEntities.forEach(scanRecordEntity -> {
            scanRecordIds.add(scanRecordEntity.getId());
        });
        VulnInfoEntity vulnInfoEntity = baseMapper.selectOne(
                new QueryWrapper<VulnInfoEntity>()
                        .in("scan_record_id", scanRecordIds)
                        .eq("id", vulnInfoId)
        );
        if (vulnInfoEntity == null) {
            return null;
        }
        String vulnId = vulnInfoEntity.getVulnId();
        JSONObject detail = vulnService.getVuln(vulnId);
        return detail;
    }

    @Override
    public List<VulnInfoEntity> getByScanRecordId(Integer scanRecordId) {
        return baseMapper.selectList(new QueryWrapper<VulnInfoEntity>().eq("scan_record_id", scanRecordId));
    }

    @Override
    public VulnInfoEntity getByVulnId(String vulnId) {
        return baseMapper.selectOne(new QueryWrapper<VulnInfoEntity>().eq("vuln_id", vulnId));
    }

}