package com.atlxc.VulnScan.product.service;

import com.atlxc.VulnScan.exception.EmailExistException;
import com.atlxc.VulnScan.exception.UserNameExistException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.product.entity.UsersEntity;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
public interface UsersService extends IService<UsersEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void Register(UsersEntity user) throws UserNameExistException,EmailExistException;

    void CheakUsername(String username) throws UserNameExistException;

    void CheckEmail(String email) throws EmailExistException;
    UsersEntity Login(UsersEntity user);
}

