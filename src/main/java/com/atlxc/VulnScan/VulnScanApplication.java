package com.atlxc.VulnScan;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

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
