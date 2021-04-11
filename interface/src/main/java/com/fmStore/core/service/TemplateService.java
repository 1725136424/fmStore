package com.fmStore.core.service;

import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TemplateService {
    PageResult getTemplateByExample(TypeTemplate typeTemplate, Integer pageSize, Integer page);

    Integer saveTemplate(TypeTemplate typeTemplate);

    Integer editTemplate(TypeTemplate typeTemplate);

    Integer deleteTemplate(Long[] idx);

    Map getSpecAndSpecOption(Long id);
}
