package com.atlxc.VulnScan.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 登录表单
 */
@Data
public class LoginForm {
    /**
     * 用户名
     */
    @Length(min = 4,max = 10,message = "用户名不正确")
    @NotEmpty(message = "用户名必须提交")
    private String username;
    /**
     * 密码
     */
    @Length(min = 6,max = 20,message = "密码不正确")
    @NotEmpty(message = "密码必须提交")
    private String password;
    /**
     * 验证码
     */
    private String captcha;

}
