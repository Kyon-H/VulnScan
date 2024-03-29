package com.atlxc.VulnScan.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.product.apiservice.ScanService;
import com.atlxc.VulnScan.product.apiservice.TargetService;
import com.atlxc.VulnScan.product.entity.ScanRecordEntity;
import com.atlxc.VulnScan.product.entity.ScanTypeEntity;
import com.atlxc.VulnScan.product.entity.VulnInfoEntity;
import com.atlxc.VulnScan.product.service.ScanRecordService;
import com.atlxc.VulnScan.product.service.ScanTypeService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;
import com.atlxc.VulnScan.vo.AddTargetVo;
import com.atlxc.VulnScan.vo.ScanPageVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Autowired
    private ScanTypeService scanTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@NotNull @Valid ScanPageVo options, @NotNull Principal principal) {
        Integer userId = usersServices.getIdByName(principal.getName());
        options.setUserId(userId);
        PageUtils page = scanRecordService.queryPage(options);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
//    @RequestMapping("/info/{id}")
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
        //setLogin
        JSONObject credentials = new JSONObject();
        JSONArray cookies = new JSONArray();
        if (vo.getUsername().length() >= 1 && vo.getPassword().length() >= 1) {
            credentials.put("enabled", true);
            credentials.put("username", vo.getUsername());
            credentials.put("password", vo.getPassword());
        }
        if (vo.getCookie().length() >= 1 && vo.getUrl().length() >= 1) {
            JSONObject cookie = new JSONObject();
            cookie.put("url", vo.getUrl());
            cookie.put("cookie", vo.getCookie());
            cookies.add(cookie);
        }
        if (credentials.size() >= 1 || cookies.size() >= 1) {
            targetService.setLogin(scanRecord.getTargetId(), credentials, cookies);
        }
        //settype
        ScanTypeEntity scanTypeEntity = scanTypeService.getById(vo.getScanType());
        if (scanTypeEntity == null) return R.error("Scan type not found");
        scanRecord.setStatus("processing");
        scanRecord.setType(vo.getScanType());
        scanRecord.setScanTime(new Date());
        JSONObject severityCounts = new JSONObject();
        severityCounts.put("high", 0);
        severityCounts.put("medium", 0);
        severityCounts.put("low", 0);
        severityCounts.put("info", 0);
        scanRecord.setSeverityCounts(severityCounts);

        JSONObject result = scanService.postScans(scanRecord);
        scanRecordService.save(scanRecord);
        connectorService.getScanRecordStatus(scanRecord.getTargetId());
        return R.ok(result);
    }

    @GetMapping("/ScanType")
    public R getScanType() {
        List<ScanTypeEntity> scanTypes = scanTypeService.getScanTypes();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", scanTypes);
        return R.ok(result);
    }

    /**
     * 修改
     */
//    @RequestMapping("/update")
    public R update(@RequestBody ScanRecordEntity scanRecord) {
        scanRecordService.updateById(scanRecord);

        return R.ok();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/all")
    public R updateAll(@NotNull Principal principal) {
        log.info("updateAll");
        String username = principal.getName();
        Integer userId = usersServices.getIdByName(username);
        scanRecordService.updateAll(userId);
        scanTypeService.updateScanType();
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id, @NotNull Principal principal) {
        Integer userId = usersServices.getIdByName(principal.getName());
        ScanRecordEntity scanRecord = scanRecordService.getById(id, userId);
        if (scanRecord == null) return R.error(400, "扫描记录不存在");
        List<VulnInfoEntity> vulnInfoEntityList = vulnInfoService.getByScanRecordId(scanRecord.getId());
        Boolean success;
        if (vulnInfoEntityList.size() == 0) {
            success = scanRecordService.removeById(scanRecord.getId());
        } else {
            success = scanRecordService.removeByIds(
                    scanRecord.getId(),
                    vulnInfoEntityList.stream().map(VulnInfoEntity::getId).collect(Collectors.toList())
            );
        }
        if (!success) {
            return R.error("删除失败");
        }
        scanService.deleteScans(scanRecord.getScanId());
        targetService.deleteTarget(scanRecord.getTargetId());
        return R.ok("删除成功");
    }

}
