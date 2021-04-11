package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.entity.SpecificationEntity;
import com.fmStore.core.pojo.specification.Specification;
import com.fmStore.core.pojo.specification.SpecificationOption;
import com.fmStore.core.service.SpecificationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecificationWeb {
    @Reference
    private SpecificationService specificationService;

    // 按照条件获取规格
    @RequestMapping("/getSpecByExample")
    public PageResult getSpecByExample(@RequestBody Specification specification, Integer pageSize, Integer page) {
        PageResult result = specificationService.getSpecByExample(specification, pageSize, page);
        return result;
    }

    // 保存规格与规格选型
    @RequestMapping("/saveSpec")
    public Result saveSpec(@RequestBody SpecificationEntity specificationEntity) {
        Result result = new Result();
        try {
            Integer count = specificationService.saveSpec(specificationEntity);
            result.setIsSuccess(true);
            result.setMessage("保存成功");
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setMessage("保存失败");
        }
        return result;
    }

    // 获取规格选项
    @RequestMapping("/getSpecOptions")
    public List<SpecificationOption> getSpecOptions(Long id) {
        List<SpecificationOption> specOptions = specificationService.getSpecOptions(id);
        return specOptions;
    }

    // 更新规格
    @RequestMapping("/editSpec")
    public Result editSpec(@RequestBody SpecificationEntity specificationEntity) {
        Result result = new Result();
        try {
            Integer count = specificationService.editSpec(specificationEntity);
            result.setIsSuccess(true);
            result.setMessage("修改成功");
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }

    // 删除规格
    @RequestMapping("/deleteSpec")
    public Result editSpec(Long[] idx) {
        System.out.println(idx);
        Result result = new Result();
        try {
            Integer count = specificationService.deleteSpec(idx);
            result.setIsSuccess(true);
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }
}
