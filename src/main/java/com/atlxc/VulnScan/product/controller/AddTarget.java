package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/product")
public class AddTarget {

    @PostMapping("/addTarget")
    public R addTarget(@RequestBody String target){
        log.debug("target:{}", target);
        return R.ok();
    }
}
