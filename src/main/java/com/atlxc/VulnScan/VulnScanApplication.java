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
@EnableAsync
@MapperScan("com.atlxc.VulnScan.product.dao")
@SpringBootApplication
public class VulnScanApplication {
	@Bean("connectorExecutor")
	public Executor connectorExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程数：线程池创建时候初始化的线程数
		executor.setCorePoolSize(10);
		// 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
		executor.setMaxPoolSize(20);
		// 缓冲队列：用来缓冲执行任务的队列
		executor.setQueueCapacity(500);
		// 允许线程的空闲时间60秒：当超过了核心线程之外的线程在空闲时间到达之后会被销毁
		executor.setKeepAliveSeconds(60);
		// 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
		executor.setThreadNamePrefix("do-something-");
		// 缓冲队列满了之后的拒绝策略：由调用线程处理（一般是主线程）
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		executor.initialize();
		return executor;
	}


	public static void main(String[] args) {
		log.info("app start");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		SpringApplication.run(VulnScanApplication.class, args);

	}

}
