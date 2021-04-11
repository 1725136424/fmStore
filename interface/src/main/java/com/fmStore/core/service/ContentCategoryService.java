package com.fmStore.core.service;

import com.fmStore.core.pojo.ad.ContentCategory;
import com.fmStore.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentCategoryService {
    PageResult getContentCat(ContentCategory contentCategory, Integer page, Integer pageSize);

    void saveContentCat(ContentCategory contentCategory);

    void editContentCat(ContentCategory contentCategory);

    void deleteContentCat(Long[] idx);

    List<ContentCategory> getAllCategory();
}
