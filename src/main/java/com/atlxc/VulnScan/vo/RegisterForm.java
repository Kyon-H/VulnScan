package com.atlxc.VulnScan.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 注册表单
 */
@Data
public class RegisterForm {
    /**
     * 用户名
     */
    @Length(min = 4, max = 10, message = "用户名长度必须在4-10之间")
    @NotEmpty(message = "用户名必须提交")
    private String username;
    /**
     * 密码
     */
    @Length(min = 4, max = 20, message = "密码长度必须在6-20之间")
    @NotEmpty(message = "密码必须提交")
    private String password;
    /**
     * 确认密码
     */
    @Length(min = 4, max = 20, message = "密码长度必须在6-20之间")
    @NotEmpty(message = "密码必须提交")
    private String repassword;
    /**
     * 邮箱地址
     */
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "email格式不正确")
    @NotEmpty(message = "email必须提交")
    private String email;
    /**
     * 验证码
     */
    private String captcha;
}
