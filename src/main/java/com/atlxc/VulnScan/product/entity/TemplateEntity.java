package com.atlxc.VulnScan.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-04-28 22:00:36
 */
@Data
@TableName("template")
public class TemplateEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模板id
     */
    @TableId
    private String templateId;
    /**
     * 模板名
     */
    private String name;

}
