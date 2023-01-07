package com.atlxc.VulnScan.form;

import lombok.Data;

/**
 * 登录表单
 */
@Data
public class LoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
}
