package com.atlxc.VulnScan.product.controller;

import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.utils.R;
import com.atlxc.VulnScan.utils.SslUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class DashboardController {

    /**
     * 信息接口
     * Method:GET
     * URL: /api/v1/info
     * @return
     * @throws Exception
     */
    @RequestMapping("/info")
    @ResponseBody
    public R info() throws Exception {
        log.info("info");
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        String url = ConfigConstant.AWVS_API_URL+"info";
        SslUtils.ignoreSsl();
        HttpEntity request=new HttpEntity(headers);
        ResponseEntity<String> response= template.exchange(url, HttpMethod.GET,request,String.class);
        log.info("result:{}", response.getBody());
        return R.ok(200,"success").put("data",response.getBody());
    }


}
