package com.atlxc.VulnScan.product.controller;

import java.security.Principal;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.product.apiservice.ScansService;
import com.atlxc.VulnScan.product.apiservice.TargetsService;
import com.atlxc.VulnScan.product.dao.UsersDao;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.vo.AddTargetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * 扫描记录表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-05 14:45:02
 */
@Slf4j
@RestController
@RequestMapping("/scan")
public class ScanRecordController {
    @Autowired
    private ScanRecordService scanRecordService;
    @Autowired
    private TargetsService targetService;
    @Autowired
    private ScansService scansService;
    @Autowired
    private UsersService usersServices;
    @Autowired
    private UsersDao usersDao;

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
    @PostMapping("/save")
    @ResponseBody
    public R save(@Valid AddTargetVo vo, Principal principal){
        log.info(vo.toString());
        Map<String, Object> param = new HashMap<String,Object>();
        param.put("address", vo.getAddress());
        param.put("description", vo.getDescription());
        Map<String, Object> map=targetService.addTargets(param);
        ScanRecordEntity scanRecord = new ScanRecordEntity();
        String username=principal.getName();
        log.info("当前操作用户为:{}",username);
        scanRecord.setUserId(usersDao.selectIdByUsername(username));
        scanRecord.setAddress(map.get("address").toString());
        scanRecord.setDescription(map.get("description").toString());
        scanRecord.setTargetId(map.get("targetId").toString());
        targetService.setSpeed(scanRecord.getTargetId(),vo.getScanSpeed());
        String type;
        switch (vo.getScanType()){
            case "12":
                type= ConfigConstant.SCAN_TYPE_HighRisk;break;
            case "13":
                type= ConfigConstant.SCAN_TYPE_SQLInjection;break;
            case "15":
                type= ConfigConstant.SCAN_TYPE_WeakPasswords;break;
            case "16":
                type= ConfigConstant.SCAN_TYPE_CrossSiteScripting;break;
            default:
                type= ConfigConstant.SCAN_TYPE_FullScan;break;
        }
        scanRecord.setStatus("processing");
        scanRecord.setType(type);
        scanRecord.setScanTime(new Date());
        JSONObject severityCounts = new JSONObject();
        severityCounts.put("high",0);
        severityCounts.put("info",0);
        severityCounts.put("low",0);
        severityCounts.put("medium",0);
        scanRecord.setSeverityCounts(severityCounts);

        Map<String, Object> result = scansService.postScans(scanRecord);
        scanRecordService.save(scanRecord);

        return R.ok(result);
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
