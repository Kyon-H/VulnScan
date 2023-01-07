package com.atlxc.VulnScan.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;



/**
 * 漏洞信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@RestController
@RequestMapping("product/vulninfo")
public class VulnInfoController {
    @Autowired
    private VulnInfoService vulnInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = vulnInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
        public R info(@PathVariable("id") Integer id){
		VulnInfoEntity vulnInfo = vulnInfoService.getById(id);

        return R.ok().put("vulnInfo", vulnInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody VulnInfoEntity vulnInfo){
		vulnInfoService.save(vulnInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody VulnInfoEntity vulnInfo){
		vulnInfoService.updateById(vulnInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Integer[] ids){
		vulnInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
