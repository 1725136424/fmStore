package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.GoodsEntity;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.item.ItemCat;
import com.fmStore.core.service.CategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryWeb {
    @Reference
    private CategoryService categoryService;

    @RequestMapping("/getCategoryByExample")
    public PageResult getCategoryByExample(@RequestBody ItemCat itemCat, Integer pageSize, Integer page) {
        PageResult result = categoryService.getCategory(itemCat, pageSize, page);
        return result;
    }

    @RequestMapping("/saveCategory")
    public Result saveCategory(@RequestBody ItemCat itemCat) {
        Result result = new Result();
        try {
            Integer count = categoryService.saveCategory(itemCat);
            result.setIsSuccess(true);
            result.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(true);
            result.setMessage("保存失败");
        }
        return result;
    }

    @RequestMapping("/editCategory")
    public Result editCategory(@RequestBody ItemCat itemCat) {
        Result result = new Result();
        try {
            Integer count = categoryService.editCategory(itemCat);
            result.setIsSuccess(true);
            result.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(true);
            result.setMessage("修改失败");
        }
        return result;

    }

    @RequestMapping("/deleteCategory")
    public Result deleteCategory(Long[] idx) {
        Result result = new Result();
        try {
            Integer count = categoryService.deleteCategory(idx);
            result.setIsSuccess(true);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(true);
            result.setMessage("删除失败");
        }
        return result;

    }

    // 获取所有分类
    @RequestMapping("/getAllCategory")
    public List<ItemCat> getAllCategory() {
        return categoryService.getAllCategory();
    }
}
