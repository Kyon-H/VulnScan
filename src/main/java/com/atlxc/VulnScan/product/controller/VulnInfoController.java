package com.atlxc.VulnScan.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;


/**
 * 漏洞信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Slf4j
@RestController
@RequestMapping("/vulninfo")
public class VulnInfoController {
    @Autowired
    private VulnInfoService vulnInfoService;
    @Autowired
    private UsersService usersService;

    /**
     * 列表
     */
    @RequestMapping(value = {"/list/{scanRecordId}/{severity}", "/list/{scanRecordId}", "/list"})
    public R list(
            @PathVariable(value = "scanRecordId", required = false) Integer scanRecordId,
            @PathVariable(value = "severity", required = false) Integer severity,
            @NotNull @RequestParam Map<String, Object> params, @NotNull Principal principal
    ) {
        Integer ueserId = usersService.getIdByName(principal.getName());
        params.put("userId", ueserId);
        params.put("scanRecordId", scanRecordId);
        params.put("severity", severity);
        PageUtils page = vulnInfoService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id, @NotNull Principal principal) {
        Integer userId = usersService.getIdByName(principal.getName());
        JSONObject detail = vulnInfoService.getDetail(id, userId);
        return R.ok().put("detail", detail);
    }

    /**
     * 保存
     */
//    @RequestMapping("/save")
    public R save(@RequestBody VulnInfoEntity vulnInfo) {
        vulnInfoService.save(vulnInfo);

        return R.ok();
    }

    /**
     * 修改
     */
//    @RequestMapping("/update")
    public R update(@RequestBody VulnInfoEntity vulnInfo) {
        vulnInfoService.updateById(vulnInfo);

        return R.ok();
    }

    @RequestMapping("/update/all")
    public R updateAll() {
        vulnInfoService.updateAll();
        return R.ok();
    }

    /**
     * 删除
     */
//    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        vulnInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
