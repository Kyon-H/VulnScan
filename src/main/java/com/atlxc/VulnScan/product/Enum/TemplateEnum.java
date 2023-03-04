package com.atlxc.VulnScan.product.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kyon-H
 * @date 2023/3/4 16:30
 */
@Getter
@AllArgsConstructor
public enum TemplateEnum {

    OWASPTop102013("11111111-1111-1111-1111-111111111119", "OWASP Top 10 2013"),
    OWASPTop102017("11111111-1111-1111-1111-111111111125", "OWASP Top 10 2017"),
    AffectedItems("11111111-1111-1111-1111-111111111115", "Affected Items"),
    CWE2011("11111111-1111-1111-1111-111111111116", "CWE 2011"),
    Developer("11111111-1111-1111-1111-111111111111", "Developer"),
    ExecutiveSummary("11111111-1111-1111-1111-111111111113", "Executive Summary"),
    HIPAA("11111111-1111-1111-1111-111111111114", "HIPAA"),
    ISO27001("11111111-1111-1111-1111-111111111117", "ISO 27001"),
    NISTSP80053("11111111-1111-1111-1111-111111111118", "NIST SP800 53"),
    PCIDSS32("11111111-1111-1111-1111-111111111120", "PCI DSS 3.2"),
    Quick("11111111-1111-1111-1111-111111111112", "Quick"),
    SarbanesOxley("11111111-1111-1111-1111-111111111121", "Sarbanes Oxley"),
    ScanComparison("11111111-1111-1111-1111-111111111124", "Scan Comparison"),
    STIGDISA("11111111-1111-1111-1111-111111111122", "STIG DISA"),
    WASCThreatClassification("11111111-1111-1111-1111-111111111123", "WASC Threat Classification");

    private final String templateId;

    private final String templateName;

    @NotNull
    public static Boolean isValidValue(String value) {
        for (TemplateEnum item : TemplateEnum.values()) {
            if (item.getTemplateId().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
