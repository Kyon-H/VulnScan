package com.atlxc.VulnScan.product.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kyon-H
 * @date 2023/2/4 11:43
 */
@Getter
@AllArgsConstructor
public enum ScanSpeedEnum {
    /**
     * 由慢到快
     */
    Sequential("sequential", "Sequential"),
    Low("slow", "Low"),
    Moderate("moderate", "Moderate"),
    Fast("fast", "Fast");

    private final String code;
    private final String message;

    /**
     * 判断参数合法性
     *
     * @param value 扫描速度
     * @return
     */
    public static Boolean isValidValue(String value) {
        for (ScanSpeedEnum b : ScanSpeedEnum.values()) {
            if (b.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
