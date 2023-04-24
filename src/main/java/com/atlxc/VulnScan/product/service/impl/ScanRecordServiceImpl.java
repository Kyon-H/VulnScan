package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.dto.ScanRecordDTO;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.dao.ScanRecordDao;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;
import com.atlxc.VulnScan.vo.AddTargetVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("scanRecordService")
public class ScanRecordServiceImpl extends ServiceImpl<ScanRecordDao, ScanRecordEntity> implements ScanRecordService {

    @Autowired
    ScanService scanService;
    @Autowired
    TargetService targetService;
    @Autowired
    VulnInfoService vulnInfoService;

    @Override
    public PageUtils queryPage(@NotNull Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        if (StringUtils.isNotEmpty((String) params.get("sidx"))) {
            Boolean isAsc = params.get("order").toString().equals("asc") ? Boolean.TRUE : Boolean.FALSE;
            //排序
            IPage<ScanRecordEntity> page = this.page(
                    new Query<ScanRecordEntity>().getPage(params, params.get("sidx").toString(), isAsc),
                    new QueryWrapper<ScanRecordEntity>().eq("user_id", userId)
            );
            return new PageUtils(page);
        }
        throw new RRException("Invalid");
    }

    @Override
    public Boolean updateStatus(Integer id, String status) {

        return baseMapper.updateStatus(id, status);
    }

    @Override
    public Boolean updateSeverity(Integer id, @NotNull JSONObject severity) {
        return baseMapper.updateSeverity(id, severity.toString());
    }

    @Override
    public String getStatusById(Integer id) {
        ScanRecordEntity entity = baseMapper.selectOne(new QueryWrapper<ScanRecordEntity>().eq("id", id));
        return entity.getStatus();
    }

    @Override
    public Boolean updateAll(Integer userId) {
        List<ScanRecordEntity> entities = baseMapper.selectList(new QueryWrapper<ScanRecordEntity>().eq("user_id", userId));
        entities.forEach(entity -> {
            ScanRecordEntity status = scanService.getStatus(entity.getScanId());
            entity.setStatus(status.getStatus());
            entity.setSeverityCounts(status.getSeverityCounts());
            entity.setScanSessionId(status.getScanSessionId());
            baseMapper.updateById(entity);
        });
        return true;
    }

    @Override
    public ScanRecordEntity addTarget(AddTargetVo vo) {
        //添加目标
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("address", vo.getAddress());
        param.put("description", vo.getDescription());
        JSONObject json = targetService.addTargets(param);
        //
        return null;
    }

    @Override
    public ScanRecordEntity getByTargetId(String targetId) {
        return baseMapper.selectOne(new QueryWrapper<ScanRecordEntity>().eq("target_id", targetId));
    }

    @Override
    public List<ScanRecordEntity> getByUserId(Integer userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<ScanRecordEntity> getByUserName(String userName) {
        return baseMapper.selectIdByUserName(userName);
    }

    @Override
    public ScanRecordEntity getById(Integer id, Integer userId) {
        return baseMapper.selectById(id, userId);
    }

    @Override
    public ScanRecordEntity getById(Integer id) {
        return baseMapper.selectOne(new QueryWrapper<ScanRecordEntity>().eq("id", id));
    }

    @Transactional
    @Override
    public Boolean removeByIds(Integer id, List<Integer> vulnIds) {
        if (vulnInfoService.removeByIds(vulnIds) && baseMapper.deleteById(id) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, String>> getMostTarget(Integer userId, Integer count) {
        List<Map<String, String>> scanRecordEntities = baseMapper.selectMostTarget(userId, count);
        return scanRecordEntities;
    }
}