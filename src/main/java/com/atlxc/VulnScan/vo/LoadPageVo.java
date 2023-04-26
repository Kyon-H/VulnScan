package com.atlxc.VulnScan.vo;

import lombok.Data;

/**
 * @author Kyon-H
 * @date 2023/4/24 0:48
 */
@Data
public class LoadPageVo {

    private Integer page;

    private Integer limit;

    private String sidx;

    private Boolean desc;

}
