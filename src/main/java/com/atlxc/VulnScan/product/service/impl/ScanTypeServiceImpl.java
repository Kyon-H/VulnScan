package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.atlxc.VulnScan.product.apiservice.ScanProfileService;
import com.atlxc.VulnScan.product.dao.ScanTypeDao;
import com.atlxc.VulnScan.product.entity.ScanTypeEntity;
import com.atlxc.VulnScan.product.service.ScanTypeService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kyon-H
 * @date 2023/4/22 16:36
 */
@Slf4j
@Service("ScanTypeService")
public class ScanTypeServiceImpl extends ServiceImpl<ScanTypeDao, ScanTypeEntity> implements ScanTypeService {

    @Autowired
    ScanProfileService scanProfileService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ScanTypeEntity> page = this.page(
                new Query<ScanTypeEntity>().getPage(params),
                new QueryWrapper<ScanTypeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean updateScanType() {
        JSONArray scanProfiles = scanProfileService.scanProfiles().getJSONArray("scanning_profiles");
        List<ScanTypeEntity> scanTypes = scanProfiles.stream().map(profile -> {
            String profileId = ((Map) profile).get("profile_id").toString();
            String name = ((Map) profile).get("name").toString();
            ScanTypeEntity scanType = new ScanTypeEntity();
            scanType.setName(name);
            scanType.setProfileId(profileId);
            return scanType;
        }).collect(Collectors.toList());
        for (ScanTypeEntity scanTypeEntity : scanTypes) {
            ScanTypeEntity selectOne = baseMapper.selectOne(new QueryWrapper<ScanTypeEntity>()
                    .eq("profile_id", scanTypeEntity.getProfileId()));
            if (selectOne == null) {
                baseMapper.insert(scanTypeEntity);
            } else {
                if (selectOne.getName().equals(scanTypeEntity.getName())) {
                    continue;
                }
                selectOne.setName(scanTypeEntity.getName());
                baseMapper.updateById(selectOne);
            }
        }
        return true;
    }

    @Override
    public List<ScanTypeEntity> getScanTypes() {
        return baseMapper.selectAll();
    }
}
