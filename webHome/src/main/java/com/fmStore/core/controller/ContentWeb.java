package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.ad.Content;
import com.fmStore.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentWeb {
    @Reference
    private ContentService contentService;

    @RequestMapping("/getContentByCat")
    private List<Content> getContentByCat(Long categoryId) {
        return contentService.getContentByCategory(categoryId);
    }

    @RequestMapping("/getContentByCatFromRedis")
    private List<Content> getContentByCatFromRedis(Long categoryId) {
        return contentService.getContentByCategoryFromRedis(categoryId);
    }
}
