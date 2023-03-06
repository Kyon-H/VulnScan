package com.atlxc.VulnScan.product.controller;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.apiservice.VulnService;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import com.atlxc.VulnScan.vo.AddTargetVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;

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
    private TargetService targetService;
    @Autowired
    private ScanService scanService;
    @Autowired
    private UsersService usersServices;
    @Autowired
    private ConnectorService connectorService;
    @Autowired
    private VulnInfoService vulnInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@NotNull @RequestParam Map<String, Object> params, @NotNull Principal principal) {
        Integer userId = usersServices.getIdByName(principal.getName());
        params.put("userId", userId);
        PageUtils page = scanRecordService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        ScanRecordEntity scanRecord = scanRecordService.getById(id);

        return R.ok().put("scanRecord", scanRecord);
    }

    /**
     * 保存
     */
    @SneakyThrows
    @PostMapping("/save")
    @ResponseBody
    public R save(@NotNull @Valid AddTargetVo vo, @NotNull Principal principal) {
        log.info("save()");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("address", vo.getAddress());
        param.put("description", vo.getDescription());
        JSONObject json = targetService.addTargets(param);
        ScanRecordEntity scanRecord = new ScanRecordEntity();
        String username = principal.getName();
        scanRecord.setUserId(usersServices.getIdByName(username));
        scanRecord.setAddress(json.getString("address"));
        scanRecord.setDescription(json.getString("description"));
        scanRecord.setTargetId(json.getString("target_id"));
        targetService.setSpeed(scanRecord.getTargetId(), vo.getScanSpeed());
        String type;
        switch (vo.getScanType()) {
            case "12":
                type = ConfigConstant.SCAN_TYPE_HighRisk;
                break;
            case "13":
                type = ConfigConstant.SCAN_TYPE_SQLInjection;
                break;
            case "15":
                type = ConfigConstant.SCAN_TYPE_WeakPasswords;
                break;
            case "16":
                type = ConfigConstant.SCAN_TYPE_CrossSiteScripting;
                break;
            default:
                type = ConfigConstant.SCAN_TYPE_FullScan;
                break;
        }
        scanRecord.setStatus("processing");
        scanRecord.setType(type);
        scanRecord.setScanTime(new Date());
        JSONObject severityCounts = new JSONObject();
        severityCounts.put("high", 0);
        severityCounts.put("medium", 0);
        severityCounts.put("low", 0);
        severityCounts.put("info", 0);
        scanRecord.setSeverityCounts(severityCounts);

        JSONObject result = scanService.postScans(scanRecord);
        scanRecordService.save(scanRecord);
        connectorService.getScanRecordStatus(scanRecord.getScanId());
        //connectorService.getStatus(scanRecord);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ScanRecordEntity scanRecord) {
        scanRecordService.updateById(scanRecord);

        return R.ok();
    }

    @GetMapping("/update/all")
    public R updateAll(@NotNull Principal principal) {
        log.info("updateAll");
        String username = principal.getName();
        Integer userId = usersServices.getIdByName(username);
        scanRecordService.updateAll(userId);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id, @NotNull Principal principal) {
        Integer userId = usersServices.getIdByName(principal.getName());
        ScanRecordEntity scanRecord = scanRecordService.getById(id, userId);
        if(scanRecord == null) return R.error(400, "扫描记录不存在");
        List<VulnInfoEntity> vulnInfoEntityList = vulnInfoService.getByScanRecordId(scanRecord.getId());
        Boolean success;
        if (vulnInfoEntityList.size()==0){
            success=scanRecordService.removeById(scanRecord.getId());
        }else{
            success=scanRecordService.removeByIds(
                    scanRecord.getId(),
                    vulnInfoEntityList.stream().map(VulnInfoEntity::getId).collect(Collectors.toList())
            );
        }
        if(!success){return R.error("删除失败");}
        scanService.deleteScans(scanRecord.getScanId());
        targetService.deleteTarget(scanRecord.getTargetId());
        return R.ok("删除成功");
    }

}
