package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DashboardController {

    @RequestMapping("/info")
    @ResponseBody
    public R info() {
        log.info("info");
        return R.ok();
    }


}
