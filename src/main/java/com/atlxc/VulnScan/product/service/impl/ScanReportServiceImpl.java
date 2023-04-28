package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ReportService;
import com.atlxc.VulnScan.product.dao.ScanReportDao;
import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.product.service.ScanReportService;
import com.atlxc.VulnScan.utils.DateUtils;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;
import com.atlxc.VulnScan.vo.ReportPageVo;
import com.atlxc.VulnScan.xss.SQLFilter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("scanReportService")
public class ScanReportServiceImpl extends ServiceImpl<ScanReportDao, ScanReportEntity> implements ScanReportService {

    @Autowired
    ReportService reportService;

    @Override
    public PageUtils queryPage(@NotNull ReportPageVo reportPageVo) {
        QueryWrapper queryWrapper = new QueryWrapper<ScanReportEntity>().eq("user_id", reportPageVo.getUserId());
        if (reportPageVo.getTemplateId() != null) {
            queryWrapper.eq("template_id", SQLFilter.sqlInject(reportPageVo.getTemplateId()));
        }
        if (reportPageVo.getListType() != null) {
            queryWrapper.eq("list_type", SQLFilter.sqlInject(reportPageVo.getListType()));
        }
        if (reportPageVo.getDate() != null) {
            Date date = DateUtils.stringToDate(reportPageVo.getDate(), DateUtils.DATE_PATTERN);
            date = DateUtils.addDateDays(date, 1);
            queryWrapper.le("generation_date", DateUtils.format(date, DateUtils.DATE_PATTERN));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("page", reportPageVo.getPage().toString());
        params.put("limit", reportPageVo.getLimit().toString());
        IPage<ScanReportEntity> page = this.page(
                new Query<ScanReportEntity>().getPage(params, reportPageVo.getSidx(), reportPageVo.getIsAsc()),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public ScanReportEntity getByReportId(String reportId) {
        return baseMapper.selectOne(new QueryWrapper<ScanReportEntity>().eq("report_id", reportId));
    }

    @Override
    public String downloadReport(ScanReportEntity scanReport, @NotNull String type) {
        log.info("downloadReport 开始下载报告");
        do {
            String filePath;
            switch (type) {
                case "pdf":
                    filePath = reportService.downloadReport(scanReport.getPdfUrl(), scanReport.getPdfUrl());
                    break;
                case "html":
                    filePath = reportService.downloadReport(scanReport.getHtmlUrl(), scanReport.getHtmlUrl());
                    break;
                default:
                    filePath = null;
            }
            if (filePath != null) {
                return filePath;
            }
            //重新获取url
            JSONObject report = reportService.getReport(scanReport.getReportId());
            JSONArray download = report.getJSONArray("download");
            scanReport.setHtmlUrl(download.getString(0).replace("/api/v1/reports/download/", ""));
            scanReport.setPdfUrl(download.getString(1).replace("/api/v1/reports/download/", ""));
            baseMapper.updateById(scanReport);
        } while (true);
    }

    @Override
    public ScanReportEntity getById(Integer id, Integer userId) {
        return baseMapper.getById(id, userId);
    }

    @Override
    public Integer removeById(Integer id, Integer userId) {
        return baseMapper.removeById(id, userId);
    }

}