package com.atlxc.VulnScan.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author Kyon-H
 * @date 2023/4/28 21:04
 */
@Data
public class ScanPageVo {

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
}
