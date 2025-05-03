package com.atlxc.VulnScan.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 登录表单
 */
@Data
public class LoginForm {
    /**
     * 用户名
     */
    @Size(min = 4, max = 10, message = "用户名不正确")
    @NotEmpty(message = "用户名必须提交")
    private String username;
    /**
     * 密码
     */
    @Size(min = 6, max = 20, message = "密码不正确")
    @NotEmpty(message = "密码必须提交")
    private String password;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码必须提交")
    @Size(min = 5, max = 5, message = "验证码不正确")
    private String captcha;

}
