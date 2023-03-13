package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Kyon-H
 * @date 2023/2/19 23:36
 */
@Slf4j
@Service
public class VulnService {
    private static final String URL = ConfigConstant.AWVS_API_URL + "vulnerabilities";

    /**
     * 获取所有漏洞信息
     * Method:GET
     * URL: /api/v1/vulnerabilities?l=20&q=status:open
     */
    public JSONObject getAllVulns(Integer l, String q) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL).append("?");
        if (l != null) {
            sb.append("l=").append(l).append("&");
        }
        if (q != null) {
            sb.append("q=status:").append(q).append("&");
        }
        JSONObject result = AWVSRequestUtils.GET(sb.toString());
        if (result == null) throw new RRException("获取漏洞信息失败");
        return result;
    }

    /**
     * 条件筛选漏洞信息
     * Method:GET
     * URL: /api/v1/vulnerabilities?q=severity:{int};status:{string};target_id:{target_id};confidence:{confidence};
     */
    public JSONObject selectVulns(@NotNull Map<String, Object> params) {
        StringBuilder sb = new StringBuilder().append(URL + "?q=");
        if (params.get("severity") != null) {
            sb.append("severity:").append(params.get("severity").toString()).append(";");
        }
        if (params.get("status") != null) {
            sb.append("status:").append(params.get("status").toString()).append(";");
        }
        if (params.get("target_id") != null) {
            sb.append("target_id:").append(params.get("target_id").toString()).append(";");
        }
        if (params.get("confidence") != null) {
            sb.append("confidence:").append(params.get("confidence").toString()).append(";");
        }
        String url = sb.toString();
        JSONObject result = AWVSRequestUtils.GET(url);
        if (result == null) throw new RRException("筛选漏洞信息失败");
        return result;
    }

    /**
     * 获取单个漏洞信息
     * Method:GET
     * URL: api/v1/vulnerabilities/{vuln_id}
     */
    public JSONObject getVuln(String vuln_id) {
        JSONObject result = AWVSRequestUtils.GET(URL + "/" + vuln_id);
        if (result == null) throw new RRException("获取单个漏洞信息失败");
        return result;
    }

    /**
     * 获取单个漏洞信息的http_response信息
     * Method:GET
     * URL: api/v1/vulnerabilities/{vuln_id}/http_response
     */
    public byte[] getHttpResponse(String vuln_id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        String url=URL + "/" + vuln_id + "/http_response";
        log.debug("http_response url:{}",url);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RRException("获取response信息失败");
        }
        byte[] body = responseEntity.getBody();

        if (body == null) throw new RRException("下载报告失败");
        return body;
    }
}
