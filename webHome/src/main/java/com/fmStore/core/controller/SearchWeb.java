package com.fmStore.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchWeb {

    @Reference
    private SearchService searchService;
    @RequestMapping("/getItem")
    public HashMap<String, Object> getItem(@RequestBody Map<String, Object> requestParam) {
        HashMap<String, Object> item = searchService.getItem(requestParam);
        return item;
    }
}
