package com.atlxc.VulnScan.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 扫描类型表
 *
 * @author Kyon-H
 * @date 2023/4/22 16:04
 */
@Data
@TableName("scan_type")
public class ScanTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 扫描配置id
     */
    @TableId
    private String profileId;

    /**
     * 扫描类型名
     */
    private String name;

}
