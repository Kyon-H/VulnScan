package com.atlxc.VulnScan.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 漏洞信息表
 *
 * @author lxc
 * @email a3171218907@qq.com
 * @date 2023-01-01 22:17:22
 */
@Data
@TableName("vuln_info")
public class VulnInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     *
     */
    private Integer scanRecordId;
    /**
     *
     */
    private String vulnId;
    /**
     * 严重性
     */
    private Integer severity;
    /**
     * 漏洞名
     */
    private String vulnerability;
    /**
     * target_address
     */
    private String targetAddress;
    /**
     * confidence
     */
    private Integer confidence;
    /**
     * last_see
     */
    private Date lastSeen;
}
