package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.product.apiservice.ReportService;
import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.product.entity.TemplateEntity;
import com.atlxc.VulnScan.product.service.ScanReportService;
import com.atlxc.VulnScan.product.service.TemplateService;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.product.service.impl.ConnectorService;
import com.atlxc.VulnScan.utils.DateUtils;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;
import com.atlxc.VulnScan.validator.group.AddGroup;
import com.atlxc.VulnScan.vo.ReportPageVo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Date;


/**
 * 扫描报告表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-02-28 20:39:25
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class ScanReportController {
    @Autowired
    private ScanReportService scanReportService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ConnectorService connectorService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@NotNull @Valid ReportPageVo options, @NotNull Principal principal) {
        Integer userId = usersService.getIdByName(principal.getName());
        options.setUserId(userId);
        PageUtils page = scanReportService.queryPage(options);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
//    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        ScanReportEntity scanReport = scanReportService.getById(id);

        return R.ok().put("scanReport", scanReport);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R add(@NotNull @Validated(AddGroup.class) @RequestBody ScanReportEntity scanReport, @NotNull Principal principal) {
        log.info("/report/sava {}", scanReport.toString());
        //保存基本数据
        String userName = principal.getName();
        Integer userId = usersService.getIdByName(userName);
        scanReport.setUserId(userId);
        scanReport.setStatus("processing");
        //保存模板信息
        TemplateEntity templateEntity = templateService.getById(scanReport.getTemplateId());
        scanReport.setTemplateName(templateEntity.getName());
        //
        scanReport.setGenerationDate(new Date());
        String reportId = reportService.addReport(scanReport);
        scanReport.setReportId(reportId);
        scanReportService.save(scanReport);
        //获取status、download_url、description
        connectorService.getReportStatus(scanReport.getReportId());
        return R.ok();
    }

    /**
     * 修改
     */
//    @RequestMapping("/update")
    public R update(@RequestBody ScanReportEntity scanReport) {
        scanReportService.updateById(scanReport);

        return R.ok();
    }

    @GetMapping("/update/all")
    public R updateAll() {
        templateService.updateTemplates();
        return R.ok();
    }

    @RequestMapping("/download")
    public void download(@RequestParam Integer id, @RequestParam String type, @NotNull Principal principal, HttpServletResponse response) {
        Integer userId = usersService.getIdByName(principal.getName());
        ScanReportEntity scanReport = scanReportService.getById(id, userId);
        if (scanReport == null) {
            return;
        }
        // 下载文件
        String path = scanReportService.downloadReport(scanReport, type);
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.format(new Date(), DateUtils.DATETIME_FILE_PATTERN))
                .append("_").append(scanReport.getTemplateName())
                .append(".").append(type);
        response.setHeader("Content-Disposition", "attachment;filename=" + sb);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (Exception e) {
            log.error("error {}", e.getMessage());
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id, @NotNull Principal principal) {
        Integer userId = usersService.getIdByName(principal.getName());
        ScanReportEntity scanReport = scanReportService.getById(id);
        if (scanReport == null) {
            return R.error(400, "报告不存在");
        }
        if (scanReportService.removeById(scanReport.getId(), userId) == 0) {
            return R.error("删除失败");
        }
        reportService.deleteReport(scanReport.getReportId());
        return R.ok("删除成功");
    }

}
