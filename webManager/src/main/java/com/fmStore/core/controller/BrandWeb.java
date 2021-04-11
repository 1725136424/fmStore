package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.good.Brand;
import com.fmStore.core.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/brand")
@RestController
public class BrandWeb {
    @Reference
    private BrandService brandService;
    // 根据条件获取品牌列表
    @RequestMapping("/getBrandByExample")
    public PageResult  findAll(@RequestBody Brand brand,Integer pageSize, Integer page) {
        return brandService.getBrandByExample(brand, pageSize, page);
    }

    //添加品牌
    @RequestMapping("/addBrand")
    public Result addBrand(@RequestBody Brand brand) {
        Result result = new Result();
        try {
            Integer count = brandService.addBrand(brand);
            result.setIsSuccess(true);
            result.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("添加失败");
        }
        return  result;
    }

    // 编辑品牌
    @RequestMapping("/editBrand")
    public Result editBrand(@RequestBody Brand brand) {
        Result result = new Result();
        try {
            Integer count = brandService.editBrand(brand);
            result.setIsSuccess(true);
            result.setMessage("编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("编辑失败");
        }
        return  result;
    }

    // 删除品牌
    @RequestMapping("/deleteBrand")
    public Result deleteBrand(Long[] idx) {
        Result result = new Result();
        try {
            Integer count = brandService.deleteBrand(idx);
            result.setIsSuccess(true);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("删除失败");
        }
        return  result;
    }

}
