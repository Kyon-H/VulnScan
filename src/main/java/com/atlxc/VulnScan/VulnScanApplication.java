package com.atlxc.VulnScan;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Slf4j
@MapperScan("com.atlxc.VulnScan.product.dao")
@SpringBootApplication
public class VulnScanApplication {

	public static void main(String[] args) {
		log.info("app start");
		SpringApplication.run(VulnScanApplication.class, args);

	}

}
