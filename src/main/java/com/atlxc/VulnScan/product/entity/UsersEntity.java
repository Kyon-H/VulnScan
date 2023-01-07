package com.atlxc.VulnScan.product.entity;

import com.atlxc.VulnScan.validator.group.AddGroup;
import com.atlxc.VulnScan.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户信息表
 * 
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Data
@TableName("users")
public class UsersEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@NotNull(groups = {UpdateGroup.class})
	private Integer id;
	/**
	 * 
	 */
	@NotBlank(groups = {AddGroup.class})
	private String username;
	/**
	 * 
	 */
	@NotBlank(groups = {AddGroup.class})
	private String password;
	/**
	 * 
	 */
	@Email
	private String email;
	/**
	 * 
	 */
	private String role;
	/**
	 * 
	 */
	private Date createTime;

}
