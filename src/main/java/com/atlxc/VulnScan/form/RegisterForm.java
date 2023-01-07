package com.atlxc.VulnScan.form;

import lombok.Data;

@Data
public class RegisterForm {
    private String username;
    private String password;
    private String repassword;
    private String captcha;
    private String uuid;
}
