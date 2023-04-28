package com.atlxc.VulnScan.product.service;

import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.vo.ReportPageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 扫描报告表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-28 20:39:25
 */
public interface ScanReportService extends IService<ScanReportEntity> {

    PageUtils queryPage(ReportPageVo reportPageVo);

    ScanReportEntity getByReportId(String reportId);

    String downloadReport(ScanReportEntity scanReport, String type);

    ScanReportEntity getById(Integer id, Integer userId);

    Integer removeById(Integer id, Integer userId);
}

