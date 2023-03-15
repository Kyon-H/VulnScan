package com.atlxc.VulnScan.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.VulnInfoService;
import com.atlxc.VulnScan.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    VulnInfoService vulnInfoService;
    @Autowired
    UsersService usersService;

    @RequestMapping("/severityCount")
    @ResponseBody
    public R info(@NotNull Principal principal) {
        log.info("info");
        Integer userId = usersService.getIdByName(principal.getName());
        JSONArray severityCount = vulnInfoService.getSeverityCount(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("result", severityCount);
        log.info(severityCount.toString());
        return R.ok(result);
    }

}
