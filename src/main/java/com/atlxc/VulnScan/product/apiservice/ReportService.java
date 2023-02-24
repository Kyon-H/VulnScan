package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Kyon-H
 * @date 2023/2/19 22:20
 */
@Slf4j
@Service
public class ReportService {
    private static final String URL = ConfigConstant.AWVS_API_URL + "reports";

    /**
     * 获取所有扫描报告
     * Method:GET
     * URL: /api/v1/reports?l={count}
     */
    public JSONObject getALLReports(int count) {
        JSONObject result = AWVSRequestUtils.GET(URL + "?l=" + count);
        if (result == null) throw new RRException("获取所有报告失败");
        return result;
    }

    /**
     * 删除单个报告
     * Method:DELETE
     * URL: /api/v1/reports/{report_id}
     */
    public void deleteReport(int report_id) {
        Boolean result = AWVSRequestUtils.DELETE(URL + "/" + report_id);
        if (!result) throw new RRException("删除报告失败");
    }
}
