package com.fmStore.core.service;

import com.fmStore.core.pojo.ad.Content;
import com.fmStore.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentService {
    PageResult getContent(Content content, Integer page, Integer pageSize);

    void saveContent(Content content);

    void deleteContent(Long[] idx);

    void editContent(Content content);

    List<Content> getContentByCategory(Long categoryId);

    List<Content> getContentByCategoryFromRedis(Long categoryId);
}
