package com.atlxc.VulnScan.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atlxc.VulnScan.product.entity.TargetInfoEntity;
import com.atlxc.VulnScan.product.service.TargetInfoService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;



/**
 * 目标记录表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-04 22:17:24
 */
@RestController
@RequestMapping("product/targetinfo")
public class TargetInfoController {
    @Autowired
    private TargetInfoService targetInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = (PageUtils) targetInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		TargetInfoEntity targetInfo = targetInfoService.getById(id);

        return R.ok().put("targetInfo", targetInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TargetInfoEntity targetInfo){
		targetInfoService.save(targetInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody TargetInfoEntity targetInfo){
		targetInfoService.updateById(targetInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		targetInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
