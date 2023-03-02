package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.product.entity.ScanReportEntity;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

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
    public JSONObject getALLReports(Integer count) {
        StringBuilder sb = new StringBuilder(URL);
        if(count!= null){
            sb.append("?l=").append(count);
        }
        JSONObject result = AWVSRequestUtils.GET(sb.toString());
        if (result == null) throw new RRException("获取所有报告失败");
        return result;
    }

    /**
     * 获取单个扫描报告
     * Method:GET
     * URL: /api/v1/reports/{report_id}
     */
    public JSONObject getReport(String reportID) {
        JSONObject result = AWVSRequestUtils.GET(URL + "/" + reportID);
        if (result == null) throw new RRException("获取单个报告失败");
        return result;
    }

    /**
     * 添加报告
     * Method:POST
     * URL: /api/v1/reports
     * 返回201状态码
     * headers: {location: "/api/v1/reports/{report_id}"}
     */
    public String addReport(ScanReportEntity scanReport) {
        JSONObject body = new JSONObject();
        JSONObject source = new JSONObject();
        JSONArray list = new JSONArray();

        list.add(scanReport.getIdList());

        source.put("list_type", scanReport.getListType());
        source.put("id_list", list);

        body.put("template_id", scanReport.getTemplateId());
        body.put("source", source);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(body, headers);

        URI uri = restTemplate.postForLocation(URL, httpEntity, JSONObject.class);
        String location = uri.toString();
        if(StringUtils.contains(location,"/api/v1/reports/")){
            return StringUtils.substring(location,16);
        }
        throw new RRException("添加报告失败");
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
