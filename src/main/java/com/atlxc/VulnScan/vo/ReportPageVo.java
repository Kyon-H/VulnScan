package com.atlxc.VulnScan.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author Kyon-H
 * @date 2023/4/28 20:11
 */
@Data
public class ReportPageVo {

    @Null
    private Integer userId;

    @NotNull
    private Integer page;

    @NotNull
    private Integer limit;

    private String sidx;

    private Boolean isAsc;

    private Integer totalCount;

    private Integer totalPage;

    private String templateId;

    @Pattern(regexp = "^(scans|vulnerabilities)$")
    private String listType;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-\\d{2}$")
    private String date;
}
