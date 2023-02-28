package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
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


@Service("scanReportService")
public class ScanReportServiceImpl extends ServiceImpl<ScanReportDao, ScanReportEntity> implements ScanReportService {

    @Autowired
    ReportService reportService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ScanReportEntity> page = this.page(
                new Query<ScanReportEntity>().getPage(params),
                new QueryWrapper<ScanReportEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addReport(ScanReportEntity scanReport) {
        reportService.addReport(scanReport);
        JSONObject jsonObject = reportService.getALLReports(null);
        JSONArray allReports=jsonObject.getJSONArray("reports");
        for(int i=0;i<allReports.size();i++){
            JSONObject report = allReports.getJSONObject(i);
            String templateId = report.getString("template_id");
            JSONObject source = report.getJSONObject("source");
        }
    }

}