package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.product.entity.UsersEntity;
import com.atlxc.VulnScan.product.service.UsersService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 用户信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@RestController
@RequestMapping("product/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    /**
     * 列表
     */
    //@RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = usersService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    //@RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        UsersEntity users = usersService.getById(id);

        return R.ok().put("users", users);
    }

    /**
     * 保存
     */
    //@RequestMapping("/save")
    public R save(@RequestBody UsersEntity users) {
        usersService.save(users);

        return R.ok();
    }

    /**
     * 修改
     */
    //@RequestMapping("/update")
    public R update(@RequestBody UsersEntity users) {
        usersService.updateById(users);

        return R.ok();
    }

    /**
     * 删除
     */
    //@RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        usersService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
