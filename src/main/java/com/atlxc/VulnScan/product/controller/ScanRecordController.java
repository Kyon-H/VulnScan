package com.atlxc.VulnScan.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;



/**
 * 扫描记录表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@RestController
@RequestMapping("product/scanrecord")
public class ScanRecordController {
    @Autowired
    private ScanRecordService scanRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = scanRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
        public R info(@PathVariable("id") Integer id){
		ScanRecordEntity scanRecord = scanRecordService.getById(id);

        return R.ok().put("scanRecord", scanRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody ScanRecordEntity scanRecord){
		scanRecordService.save(scanRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody ScanRecordEntity scanRecord){
		scanRecordService.updateById(scanRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Integer[] ids){
		scanRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
