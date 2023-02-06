/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.atlxc.VulnScan.config;

/**
 * 系统参数相关Key
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ConfigConstant {
    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";
    /**
     * AWVS api路径
     */
    public static final String AWVS_API_URL="https://desktop-jv0cb08:3443/api/v1/";
    public final static String AWVS_API_KEY="1986ad8c0a5b3df4d7028d5f3c06e936cd85c6c2e5fac4f398dde2b38c66881d4";
    /**
     * 完全扫描
     */
    public static final String SCAN_TYPE_FullScan="11111111-1111-1111-1111-111111111111";
    /**
     * 高风险漏洞
     */
    public static final String SCAN_TYPE_HighRisk="11111111-1111-1111-1111-111111111112";
    /**
     * SQL 注入漏洞
     */
    public static final String SCAN_TYPE_SQLInjection="11111111-1111-1111-1111-111111111113";
    /**
     * 弱口令检测
     */
    public static final String SCAN_TYPE_WeakPasswords="11111111-1111-1111-1111-111111111115";
    /**
     * XSS 漏洞
     */
    public  static final String SCAN_TYPE_CrossSiteScripting="11111111-1111-1111-1111-111111111116";
    /**
     * Crawl Only
     */
    public static final String SCAN_TYPE_Crawl_Only="11111111-1111-1111-1111-111111111117";
    /**
     * 恶意软件扫描
     */
    public static final String SCAN_TYPE_Malware_Scan="11111111-1111-1111-1111-111111111120";

}
