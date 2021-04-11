package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.template.TypeTemplate;
import com.fmStore.core.service.TemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template")
public class TemplateWeb {
    @Reference
    private TemplateService templateService;

    @RequestMapping("/getTemplateByExample")
    public PageResult getTemplateByExample(@RequestBody TypeTemplate typeTemplate, Integer pageSize, Integer page) {
        PageResult templateByExample = templateService.getTemplateByExample(typeTemplate, pageSize, page);
        return templateByExample;
    }

    @RequestMapping("/saveTemplate")
    public Result saveTemplate(@RequestBody TypeTemplate typeTemplate) {
        Result result = new Result();
        try {
            result.setIsSuccess(true);
            result.setMessage("保存成功");
            Integer count = templateService.saveTemplate(typeTemplate);
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setMessage("保存失败");
        }
        return result;
    }

    @RequestMapping("/editTemplate")
    public Result editTemplate(@RequestBody TypeTemplate typeTemplate) {
        Result result = new Result();
        try {
            result.setIsSuccess(true);
            result.setMessage("修改成功");
            Integer count = templateService.editTemplate(typeTemplate);
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }

    @RequestMapping("/deleteTemplate")
    public Result deleteTemplate(Long[] idx) {
        Result result = new Result();
        try {
            result.setIsSuccess(true);
            result.setMessage("删除成功");
            Integer count = templateService.deleteTemplate(idx);
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }
}
