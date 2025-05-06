package com.atlxc.VulnScan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Kyon-H
 * @date 2025/5/8 17:28
 */
@Builder
@Data
public class UserInfoDTO {

    private Integer id;

    private String username;

    private String email;

    private String role;

    private Date createTime;
}
