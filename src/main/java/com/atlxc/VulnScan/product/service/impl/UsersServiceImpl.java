package com.atlxc.VulnScan.product.service.impl;

import com.atlxc.VulnScan.exception.EmailExistException;
import com.atlxc.VulnScan.exception.UserNameExistException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;

import com.atlxc.VulnScan.product.dao.UsersDao;
import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.atlxc.VulnScan.product.service.UsersService;


@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UsersEntity> page = this.page(
                new Query<UsersEntity>().getPage(params),
                new QueryWrapper<UsersEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void Register(UsersEntity user) throws UserNameExistException, EmailExistException {
        UsersEntity users = new UsersEntity();
        CheakUsername(user.getUsername());
        users.setUsername(user.getUsername());
        // 密码加密存储
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        users.setPassword(password);
        users.setRole(user.getRole());
        users.setEmail(user.getEmail());
        users.setCreateTime(new Date());
        baseMapper.insert(users);
    }

    @Override
    public void CheakUsername(String username) throws UserNameExistException {
        Long count = this.baseMapper.selectCount(new QueryWrapper<UsersEntity>().eq("username", username));
        if(count>0) throw new UserNameExistException();
    }

    @Override
    public void CheckEmail(String email) throws EmailExistException {

    }


    /**
     * 用户登录验证
     * @param user
     * @return
     */
    @Override
    public UsersEntity Login(UsersEntity user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UsersEntity entity = baseMapper.selectOne(new QueryWrapper<UsersEntity>().eq("username", user.getUsername()));
        if(entity==null){
            //登录失败
            return null;
        }else {
            boolean matches = bCryptPasswordEncoder.matches(user.getPassword(),entity.getPassword());
            if (matches) {
                entity.setPassword(null);
                return entity;
            }else {
                return null;
            }
        }
    }

}