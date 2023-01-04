package com.atlxc.VulnScan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.atlxc.VulnScan.product.dao")
@SpringBootApplication
public class VulnScanApplication {

	public static void main(String[] args) {
		SpringApplication.run(VulnScanApplication.class, args);
	}

}
