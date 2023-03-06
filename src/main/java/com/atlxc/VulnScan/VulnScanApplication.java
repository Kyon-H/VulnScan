package com.atlxc.VulnScan;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@Slf4j
@MapperScan("com.atlxc.VulnScan.product.dao")
@SpringBootApplication
public class VulnScanApplication {

    public static void main(String[] args) {
        log.info("app start");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SpringApplication.run(VulnScanApplication.class, args);

    }

}
