package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.form.LoginForm;
import com.atlxc.VulnScan.form.RegisterForm;
import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.utils.R;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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



    @PostMapping("/Register")
    @ResponseBody
    public R Register(@Valid RegisterForm registerForm){
        log.info("Register({})",registerForm);
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

    @PostMapping("/Login")
    @ResponseBody
    public R Login(HttpServletRequest request, @Valid LoginForm loginForm){
        log.info("Login");
        //String sessionCode=(String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //if(sessionCode.equalsIgnoreCase((String)form.getCaptcha())){
            UsersEntity usersEntity = new UsersEntity();
//            usersEntity.setUsername(form.getUsername());
//            usersEntity.setPassword(form.getPassword());
        usersEntity.setUsername(loginForm.getUsername());
        usersEntity.setPassword(loginForm.getPassword());
            UsersEntity result=usersService.Login(usersEntity);
            if(result==null){
                return R.error("账号或密码不正确...");
            }

            return R.ok(200,"登录成功");
        //}else{
        //    return R.error("验证码不正确");
        //}


    }
}
