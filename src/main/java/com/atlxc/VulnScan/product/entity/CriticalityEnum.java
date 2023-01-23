package com.atlxc.VulnScan.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CriticalityEnum {

    Low(0,"Low"),
    Normal(10,"Normal"),
    High(20,"High"),
    Critical(30,"Critical");

    private final Integer code;
    private final String message;

    /**
     * 判断参数合法性
     * @param value 危险等级
     * @return
     */
    public static Boolean isValidValue(Integer value) {
        for (CriticalityEnum b : CriticalityEnum.values()) {
            if (b.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
