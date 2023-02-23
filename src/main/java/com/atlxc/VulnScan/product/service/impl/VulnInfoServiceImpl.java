package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.VulnService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.utils.DateUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
    UsersService usersService;
    @Autowired
    ScanRecordService scanRecordService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String userName=params.get("userName").toString();
        List<ScanRecordEntity> scanRecord=scanRecordService.getByUserName(userName);
        List<Integer>scanRecordIds=new ArrayList<>();
        scanRecord.forEach(scanRecordEntity -> {
            Integer scanRecordId=scanRecordEntity.getId();
            scanRecordIds.add(scanRecordId);
        });
        IPage<VulnInfoEntity> page = this.page(
                new Query<VulnInfoEntity>().getPage(params),
                new QueryWrapper<VulnInfoEntity>().in("scan_record_id",scanRecordIds)
        );
        return new PageUtils(page);
    }

    @Override
    public Boolean updateAll() {
        log.info("updateAll");
        Integer counts = vulnService.getAllVulns(1, "open").getJSONObject("pagination").getInteger("count");
        while (counts>0){
            Integer l=counts>=20?20:counts;
            JSONArray jsonArray = vulnService.getAllVulns(l, "open").getJSONArray("vulnerabilities");
            for(int i=0;i<jsonArray.size();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String targetId=item.getString("target_id");
                ScanRecordEntity scanRecord=scanRecordService.getByTargetId(targetId);
                if(scanRecord!=null){
                    VulnInfoEntity vulnInfo;
                    vulnInfo=baseMapper.selectOne(new QueryWrapper<VulnInfoEntity>().eq("vuln_id",item.getString("vuln_id")));
                    if(vulnInfo==null){
                        vulnInfo=new VulnInfoEntity();
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
            counts-=20;
            }
        return Boolean.TRUE;
    }

}