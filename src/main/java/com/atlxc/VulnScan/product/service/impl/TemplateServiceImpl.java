package com.atlxc.VulnScan.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.atlxc.VulnScan.product.apiservice.ReportService;
import com.atlxc.VulnScan.product.dao.TemplateDao;
import com.atlxc.VulnScan.product.entity.TemplateEntity;
import com.atlxc.VulnScan.product.service.TemplateService;
import com.atlxc.VulnScan.utils.PageUtils;
import com.atlxc.VulnScan.utils.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("templateService")
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, TemplateEntity> implements TemplateService {

    @Autowired
    ReportService reportService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TemplateEntity> page = this.page(
                new Query<TemplateEntity>().getPage(params),
                new QueryWrapper<TemplateEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean updateTemplates() {
        JSONArray templateArray = reportService.getTemplates().getJSONArray("templates");
        List<TemplateEntity> templates = templateArray.stream().map(template -> {
            String templateId = ((Map) template).get("template_id").toString();
            String name = ((Map) template).get("name").toString();
            TemplateEntity templateEntity = new TemplateEntity();
            templateEntity.setTemplateId(templateId);
            templateEntity.setName(name);
            return templateEntity;
        }).collect(Collectors.toList());
        for (TemplateEntity templateEntity : templates) {
            TemplateEntity selectOne = baseMapper.selectOne(new QueryWrapper<TemplateEntity>()
                    .eq("template_id", templateEntity.getTemplateId()));
            if (selectOne == null) {
                baseMapper.insert(templateEntity);
            } else {
                if (selectOne.getName().equals(templateEntity.getName())) {
                    continue;
                }
                selectOne.setName(templateEntity.getName());
                baseMapper.updateById(selectOne);
            }
        }
        return true;
    }

}