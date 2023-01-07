package com.atlxc.VulnScan.product.controller;

import java.util.Arrays;
import java.util.Map;

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
 * @date 2023-01-01 22:17:22
 */
@RestController
@RequestMapping("product/scanreport")
public class ScanReportController {
    @Autowired
    private ScanReportService scanReportService;

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
    @RequestMapping("/save")
        public R save(@RequestBody ScanReportEntity scanReport){
		scanReportService.save(scanReport);

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
