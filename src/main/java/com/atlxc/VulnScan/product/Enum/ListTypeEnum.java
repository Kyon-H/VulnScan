package com.atlxc.VulnScan.product.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kyon-H
 * @date 2023/3/4 17:03
 */
@Getter
@AllArgsConstructor
public enum ListTypeEnum {
    Scans("scans"),
    Vulnerabilities("vulnerabilities");

    private final String code;

    @NotNull
    public static Boolean isValidValue(String value) {
        for (ListTypeEnum item : ListTypeEnum.values()) {
            if (item.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
