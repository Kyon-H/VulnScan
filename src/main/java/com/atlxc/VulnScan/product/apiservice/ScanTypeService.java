package com.atlxc.VulnScan.product.apiservice;

import com.alibaba.fastjson.JSONObject;
import com.atlxc.VulnScan.config.ConfigConstant;
import com.atlxc.VulnScan.exception.RRException;
import com.atlxc.VulnScan.utils.AWVSRequestUtils;

/**
 * @author Kyon-H
 * @date 2023/4/22 15:27
 */
public class ScanTypeService {

    private static final String URL = ConfigConstant.AWVS_API_URL + "scanning_profiles";

    /**
     * 获取扫描类型
     * Method:GET
     * URL: api/v1/scanning_profiles
     */
    public JSONObject scanProfiles() {
        JSONObject result= AWVSRequestUtils.GET(URL);
        if(result==null){
            throw new RRException("get scanning_profiles error");
        }
        return result;
    }
}
