package com.atlxc.VulnScan.product.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.atlxc.VulnScan.product.apiservice.ReportService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import com.atlxc.VulnScan.validator.group.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.product.service.ScanReportService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;



/**
 * 扫描报告表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-28 20:39:25
 */
@RestController
@RequestMapping("/report")
public class ScanReportController {
    @Autowired
    private ScanReportService scanReportService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ConnectorService connectorService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,Principal principal){
        Integer userId = usersService.getIdByName(principal.getName());
        params.put("userId",userId);
        PageUtils page = scanReportService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		ScanReportEntity scanReport = scanReportService.getById(id);

        return R.ok().put("scanReport", scanReport);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R add(@Validated(AddGroup.class) @RequestBody ScanReportEntity scanReport, Principal principal){
        //保存基本数据
        String userName= principal.getName();
        Integer userId = usersService.getIdByName(userName);
        scanReport.setUserId(userId);
        scanReport.setStatus("processing");
        scanReport.setGenerationDate(new Date());
        String reportId=reportService.addReport(scanReport);
        scanReport.setReportId(reportId);
        scanReportService.save(scanReport);
        //获取status、download_url、description
        connectorService.getReportStatus(scanReport.getReportId());
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ScanReportEntity scanReport){
		scanReportService.updateById(scanReport);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		scanReportService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
