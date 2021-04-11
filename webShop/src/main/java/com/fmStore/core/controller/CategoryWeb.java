package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.PageResult;
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
    // 获取商品分类
    @RequestMapping("/getCategoryByExample")
    public PageResult getCategoryByExample(@RequestBody ItemCat itemCat, Integer pageSize, Integer page) {
        PageResult result = categoryService.getCategory(itemCat, pageSize, page);
        return result;
    }
    // 获取所有分类
    @RequestMapping("/getAllCategory")
    public List<ItemCat> getAllCategory() {
        return categoryService.getAllCategory();
    }
}
