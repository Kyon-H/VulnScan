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
import com.atlxc.VulnScan.xss.SQLFilter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Slf4j
@Service("vulnInfoService")
public class VulnInfoServiceImpl extends ServiceImpl<VulnInfoDao, VulnInfoEntity> implements VulnInfoService {
    @Autowired
    VulnService vulnService;
    @Autowired
    ScanRecordService scanRecordService;

    @Override
    public PageUtils queryPage(@NotNull Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        MPJQueryWrapper<VulnInfoEntity> queryWrapper = new MPJQueryWrapper<>();
//        基本查询
        queryWrapper.selectAll(VulnInfoEntity.class)
                .leftJoin("scan_record on t.scan_record_id = scan_record.id")
                .eq("scan_record.user_id", userId);
//        可变条件
        if (params.get("scanRecordId") != null) {
            queryWrapper.eq("t.scan_record_id", Integer.parseInt(params.get("scanRecordId").toString()));
        }
        if (params.get("severity") != null) {
            queryWrapper.eq("severity", Integer.parseInt(params.get("severity").toString()));
        }
        if(params.get("vulnerability") != null) {
            queryWrapper.like("vulnerability", SQLFilter.sqlInject(params.get("vulnerability").toString()));
        }
        Boolean isAsc = params.get("isAsc") == null || params.get("order").toString().equals("asc");
        String sidx = params.get("sidx") == null ? "" : params.get("sidx").toString();
//        分页查询
//        params: page, limit
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
    @SneakyThrows
    public JSONObject getDetail(Integer vulnInfoId, Integer userId) {
        VulnInfoEntity vulnInfoEntity = baseMapper.selectOneByIds(userId, vulnInfoId);
        if (vulnInfoEntity == null) {
            return null;
        }
        String vulnId = vulnInfoEntity.getVulnId();
        JSONObject result = vulnService.getVuln(vulnId);
        JSONObject detail = new JSONObject();
        detail.put("vt_name", result.getString("vt_name"));
        detail.put("description", result.getString("description"));
        detail.put("affects_url", result.getString("affects_url"));
        detail.put("request", result.getString("request"));
        detail.put("impact", result.getString("impact"));
        detail.put("recommendation", result.getString("recommendation"));
        detail.put("long_description", result.getString("long_description"));
        detail.put("details", result.getString("details"));
        detail.put("references", result.getJSONArray("references"));
        detail.put("tags", result.getJSONArray("tags"));
        detail.put(("cvss_score"), result.getString("cvss_score"));
        detail.put("cvss3", result.getString("cvss3"));
        detail.put("cvss2", result.getString("cvss2"));
        //获取httpresponse
        byte[] httpResponse = vulnService.getHttpResponse(vulnId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(httpResponse));
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = gis.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, len);
        }
        detail.put("http_response", baos.toString());
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

    @Override
    public JSONArray getSeverityCount(Integer userId) {
        List<Map<String, Integer>> maps = baseMapper.selectCountByUserId(userId);
        String jsonString = JSONObject.toJSONString(maps);
        return JSONArray.parseArray(jsonString);
    }

    @Override
    public JSONArray getTopVuln(Integer userId, Integer topNumber) {
        List<Map<String, Integer>> maps = baseMapper.selectTopVuln(userId, topNumber);
        String jsonString = JSONObject.toJSONString(maps);
        return JSONArray.parseArray(jsonString);
    }

}