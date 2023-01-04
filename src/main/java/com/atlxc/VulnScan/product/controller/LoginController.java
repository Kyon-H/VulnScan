package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.exception.UserNameExistException;
import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户登录、注册
 */
@RestController
public class LoginController {

    @Autowired
    UsersService usersService;

    @PostMapping("/register")
    public R Register(@Valid @RequestBody UsersEntity param){
        usersService.Register(param);
        return R.ok("注册成功");
    }

    @PostMapping("/login")
    public R Login(@Valid @RequestBody UsersEntity param){

        UsersEntity usersEntity=usersService.Login(param);
        if(usersEntity!=null){
            return R.ok("登录成功");
        }else {
            return R.error("登录失败");
        }

    }
}
