package com.atlxc.VulnScan.product.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kyon-H
 * @date 2023/2/4 12:14
 */
@Getter
@AllArgsConstructor
public enum ScanTypeEnum {

    FullScan("11","Full Scan"),
    HighRiskVulnerabilities("12","High Risk Vulnerabilities"),
    SQLInjectionVulnerabilities("13","SQL Injection Vulnerabilities"),
    WeakPasswords("15","Weak Passwords"),
    CrossSiteScriptingVulnerabilities("16","Cross-site Scripting Vulnerabilities");

    private final String code;
    private final String message;

    @NotNull
    public static Boolean isValidValue(String value){
        for(ScanTypeEnum item : ScanTypeEnum.values()){
            if(item.getCode().equals(value)){
                return true;
            }
        }
        return false;
    }
}
