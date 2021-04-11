package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.ad.ContentCategory;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.service.ContentCategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contentCat")
public class ContentCategoryWeb {
    @Reference
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/getContentCat")
    public PageResult getContentCat(@RequestBody ContentCategory contentCategory, Integer page, Integer pageSize) {
        return contentCategoryService.getContentCat(contentCategory, page, pageSize);
    }

    @RequestMapping("/saveContentCat")
    public Result saveContentCat(@RequestBody ContentCategory contentCategory) {
        Result result = new Result();
        try {
            contentCategoryService.saveContentCat(contentCategory);
            result.setIsSuccess(true);
            result.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("保存失败");
        }
        return result;
    }

    @RequestMapping("/deleteContentCat")
    public Result deleteContentCat(Long[] idx) {
        Result result = new Result();
        try {
            contentCategoryService.deleteContentCat(idx);
            result.setIsSuccess(true);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }

    @RequestMapping("/editContentCat")
    public Result editContentCat(@RequestBody ContentCategory contentCategory) {
        Result result = new Result();
        try {
            contentCategoryService.editContentCat(contentCategory);
            result.setIsSuccess(true);
            result.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }

    @RequestMapping("/getAllCategory")
    public List<ContentCategory> getAllCategory() {
        return contentCategoryService.getAllCategory();
    }
}
