package com.atlxc.VulnScan.product.service.impl;

import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.atlxc.VulnScan.product.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersService usersService;

    /**
     *
     * @param username 前端传入的用户名
     * @return  User(用户名，数据库中密码，权限)
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername({})",username);
        //权限数组
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //从数据库中取出用户信息
        UsersEntity users=usersService.getOne(new QueryWrapper<UsersEntity>().eq("username",username));
        //判断用户是否存在
        if(users==null) {
            log.info(username+"用户不存在");
            throw new UsernameNotFoundException("用户不存在！");
        }
        //添加权限
        authorities.add(new SimpleGrantedAuthority("ROLE_"+users.getRole()));
        // 返回UserDetails实现类,之后springsecurity自动进行密码匹配
        return new User(users.getUsername(), users.getPassword(), authorities);
    }


}
