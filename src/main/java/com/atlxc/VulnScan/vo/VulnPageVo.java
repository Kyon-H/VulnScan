package com.atlxc.VulnScan.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author Kyon-H
 * @date 2023/4/24 0:48
 */
@Data
public class VulnPageVo {

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

    private Integer scanRecordId;

    @Max(3)
    @Min(0)
    private Integer severity;

    private String vulnerability;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-\\d{2}$")
    private String date;
}
