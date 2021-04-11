package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.ad.Content;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentWeb {
    @Reference
    private ContentService contentService;

    @RequestMapping("/getContent")
    public PageResult getContent(@RequestBody Content content, Integer page, Integer pageSize) {
        return contentService.getContent(content, page, pageSize);
    }

    @RequestMapping("/saveContent")
    public Result saveContent(@RequestBody Content content) {
        Result result = new Result();
        try {
            contentService.saveContent(content);
            result.setIsSuccess(true);
            result.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("保存失败");
        }
        return result;
    }

    @RequestMapping("/deleteContent")
    public Result deleteContent(Long[] idx) {
        Result result = new Result();
        try {
            contentService.deleteContent(idx);
            result.setIsSuccess(true);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }

    @RequestMapping("/editContent")
    public Result editContent(@RequestBody Content content) {
        Result result = new Result();
        try {
            contentService.editContent(content);
            result.setIsSuccess(true);
            result.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }
}
