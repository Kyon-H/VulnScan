package com.atlxc.VulnScan.vo;

import com.atlxc.VulnScan.product.Enum.ScanSpeedEnum;
import com.atlxc.VulnScan.utils.EnumValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 添加目标接口
 * username
 * address
 * scanType
 * scanSpeed
 * description
 */
@Data
public class AddTargetVo {

    /**
     * 目标网址:需http或https开头
     */
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "URL 格式无效")
    @NotNull(message = "地址不能为空")
    private String address;
    /**
     * 扫描类型;
     */
    @NotNull(message = "扫描类型不能为空")
    //@EnumValue(enumClass = ScanTypeEnum.class, enumMethod = "isValidValue", message = "扫描类型错误")
    @Size(min = 30, max = 40)
    private String scanType;
    /**
     * 扫描速度;
     */
    @NotNull(message = "扫描速度不能为空")
    @EnumValue(enumClass = ScanSpeedEnum.class, enumMethod = "isValidValue", message = "扫描速度错误")
    private String scanSpeed;
    /**
     * login
     */
    @NotNull
    private String username;
    @NotNull
    private String password;
    /**
     * cookie
     */
    @NotNull
    private String url;
    @NotNull
    private String cookie;
    /**
     * 备注
     */
    @NotNull
    private String description;
}
