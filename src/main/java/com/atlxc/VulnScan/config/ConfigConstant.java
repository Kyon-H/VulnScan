package com.atlxc.VulnScan.config;

/**
 * 系统参数相关Key
 *
 */
public class ConfigConstant {
    /**
     * AWVS api路径
     */
    public static final String AWVS_API_URL = "https://desktop-jv0cb08:3443/api/v1/";
    public final static String AWVS_API_KEY = "1986ad8c0a5b3df4d7028d5f3c06e936cd85c6c2e5fac4f398dde2b38c66881d4";
    /**
     * 完全扫描
     */
    public static final String SCAN_TYPE_FullScan = "11111111-1111-1111-1111-111111111111";
    /**
     * 高风险漏洞
     */
    public static final String SCAN_TYPE_HighRisk = "11111111-1111-1111-1111-111111111112";
    /**
     * SQL 注入漏洞
     */
    public static final String SCAN_TYPE_SQLInjection = "11111111-1111-1111-1111-111111111113";
    /**
     * 弱口令检测
     */
    public static final String SCAN_TYPE_WeakPasswords = "11111111-1111-1111-1111-111111111115";
    /**
     * XSS 漏洞
     */
    public static final String SCAN_TYPE_CrossSiteScripting = "11111111-1111-1111-1111-111111111116";
    /**
     * Crawl Only
     */
    public static final String SCAN_TYPE_Crawl_Only = "11111111-1111-1111-1111-111111111117";
    /**
     * 恶意软件扫描
     */
    public static final String SCAN_TYPE_Malware_Scan = "11111111-1111-1111-1111-111111111120";
    /**
     * AWVS templateId
     */
    public static final String templateId_OWASPTop102013 = "11111111-1111-1111-1111-111111111119";
    public static final String templateId_OWASPTop102017 = "11111111-1111-1111-1111-111111111125";
    public static final String templateId_AffectedItems = "11111111-1111-1111-1111-111111111115";
    public static final String templateId_CWE2011 = "11111111-1111-1111-1111-111111111116";
    public static final String templateId_Developer = "11111111-1111-1111-1111-111111111111";
    public static final String templateId_ExecutiveSummary = "11111111-1111-1111-1111-111111111113";
    public static final String templateId_HIPAA = "11111111-1111-1111-1111-111111111114";
    public static final String templateId_ISO27001 = "11111111-1111-1111-1111-111111111117";
    public static final String templateId_NISTSP80053 = "11111111-1111-1111-1111-111111111118";
    public static final String templateId_PCIDSS32 = "11111111-1111-1111-1111-111111111120";
    public static final String templateId_Quick = "11111111-1111-1111-1111-111111111112";
    public static final String templateId_SarbanesOxley = "11111111-1111-1111-1111-111111111121";
    public static final String templateId_ScanComparison = "11111111-1111-1111-1111-111111111124";
    public static final String templateId_STIGDISA = "11111111-1111-1111-1111-111111111122";
    public static final String templateId_WASCThreatClassification = "11111111-1111-1111-1111-111111111123";
    /**
     * AWVS templateName
     */
    public static final String templateName_OWASPTop102013 = "OWASP Top 10 2013";
    public static final String templateName_OWASPTop102017 = "OWASP Top 10 2017";
    public static final String templateName_AffectedItems = "Affected Items";
    public static final String templateName_CWE2011 = "CWE 2011";
    public static final String templateName_Developer = "Developer";
    public static final String templateName_ExecutiveSummary = "Executive Summary";
    public static final String templateName_HIPAA = "HIPAA";
    public static final String templateName_ISO27001 = "ISO 27001";
    public static final String templateName_NISTSP80053 = "NIST SP800 53";
    public static final String templateName_PCIDSS32 = "PCI DSS 3.2";
    public static final String templateName_Quick = "Quick";
    public static final String templateName_SarbanesOxley = "Sarbanes Oxley";
    public static final String templateName_ScanComparison = "Scan Comparison";
    public static final String templateName_STIGDISA = "STIG DISA";
    public static final String templateName_WASCThreatClassification = "WASC Threat Classification";
    /**
     * filedownload
     */
    public static final String FILE_PATH = "D:/upload/";
}
