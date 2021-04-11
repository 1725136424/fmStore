package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.service.TemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/template")
public class TemplateWeb {
    @Reference
    private TemplateService templateService;

    @RequestMapping("/getSpecAndBrand")
    public Map getSpecAndSpecOption(Long id) {
        Map specAndSpecOption = templateService.getSpecAndSpecOption(id);
        return specAndSpecOption;
    }
}
