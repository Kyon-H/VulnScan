package com.atlxc.VulnScan.product.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

import com.atlxc.VulnScan.product.apiservice.ReportService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("report")
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
    public R list(@RequestParam Map<String, Object> params){
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
    @RequestMapping("/add")
    public R add(@RequestBody ScanReportEntity scanReport, Principal principal){
        String userName= principal.getName();
        Integer userId = usersService.getIdByName(userName);
        scanReport.setUserId(userId);
        scanReportService.addReport(scanReport);
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
