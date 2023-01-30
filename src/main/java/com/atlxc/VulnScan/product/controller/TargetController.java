package com.atlxc.VulnScan.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.product.apiservice.TargetsService;
import com.atlxc.VulnScan.utils.R;
import com.atlxc.VulnScan.utils.SslUtils;
import com.atlxc.VulnScan.vo.AddTargetVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
public class TargetController {

    @Autowired
    private TargetsService targetsService;
    @GetMapping("/getTarget")
    public R getTarget() {
        JSONObject object=targetsService.getTargets();
        return R.ok(object);
    }
    @RequestMapping("/addTarget")
    public R addTarget(@Valid @RequestBody AddTargetVo param) throws Exception {
        log.info("target:{}", param);

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth", ConfigConstant.AWVS_API_KEY);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        JSONObject object=new JSONObject();
        object.put("address",param.getAddress());
        object.put("description", param.getDescription());
        object.put("criticality", param.getCriticality());
        HttpEntity<String> entity = new HttpEntity<String>(object.toString(),headers);
        String url = ConfigConstant.AWVS_API_URL+"targets";
        SslUtils.ignoreSsl();
        JSONObject result = template.postForObject(url, entity,JSONObject.class);
        log.info("result:{}", result.toString());
        return R.ok().put("result", result);
    }
}
