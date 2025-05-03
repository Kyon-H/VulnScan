package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Kyon-H
 * @date 2023/4/22 15:27
 */
@Service
public class ScanProfileService {

    private final String URL;

    @Autowired
    private ScanProfileService(@Value("${api.url}") String apiUrl) {
        URL = apiUrl + "scanning_profiles";
    }

    /**
     * 获取扫描类型
     * Method:GET
     * URL: api/v1/scanning_profiles
     */
    public JSONObject scanProfiles() {
        JSONObject result = AWVSRequestUtils.GET(URL);
        if (result == null) {
            throw new RRException("get scanning_profiles error");
        }
        return result;
    }
}
