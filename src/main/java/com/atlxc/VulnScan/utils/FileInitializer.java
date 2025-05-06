package com.atlxc.VulnScan.utils;

import com.atlxc.VulnScan.product.service.ScanTypeService;
import com.atlxc.VulnScan.product.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

/**
 * @author Kyon-H
 * @date 2025/4/30 9:56
 */
@Slf4j
@Component
public class FileInitializer implements CommandLineRunner {
    @Autowired
    private ScanTypeService scanTypeService;
    @Autowired
    private TemplateService templateService;
    @Value("${init.file.path}")
    private String initFilePath;

    @Override
    public void run(String... args) throws Exception {
        File initFile = new File(initFilePath);
        if (!initFile.exists()) {
            log.info("开始初始化......");
            initialize();
            Files.createFile(initFile.toPath());
            log.info("初始化完成");
        } else {
            log.info("非首次启动，跳过初始化");
        }
    }

    private void initialize() {
        log.info("init scanType");
        int count = scanTypeService.updateScanType();
        log.info("scantype count:{}", count);
        //
        log.info("init report template");
        count = templateService.updateTemplates();
        log.info("templates count:{}", count);
        //
        File upload = new File(Constant.FILE_PATH);
        upload.mkdir();
        log.info("create upload dir{}", upload.getPath());
    }
}
