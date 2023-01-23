package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.vo.LoginForm;
import com.atlxc.VulnScan.vo.RegisterForm;
import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户登录、注册
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    UsersService usersService;



    @PostMapping("/doRegister")
    @ResponseBody
    public R Register(@Valid RegisterForm registerForm){
        log.info("Register {}",registerForm);
        if(!registerForm.getPassword().equals(registerForm.getRepassword())){
            return R.error("注册失败");
        }
        UsersEntity usersEntity=new UsersEntity();
        usersEntity.setUsername(registerForm.getUsername());
        usersEntity.setPassword(registerForm.getPassword());
        usersEntity.setEmail(registerForm.getEmail());
        usersEntity.setRole("USER");
        usersService.Register(usersEntity);
        return R.ok(200,"注册成功");
    }

}
