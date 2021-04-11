package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.ad.ContentCategoryDao;
import com.fmStore.core.pojo.ad.ContentCategory;
import com.fmStore.core.pojo.ad.ContentCategoryQuery;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.good.Brand;
import com.fmStore.core.pojo.good.BrandQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private ContentCategoryDao contentCategoryDao;

    @Override
    public PageResult getContentCat(ContentCategory contentCategory, Integer page, Integer pageSize) {
        ContentCategoryQuery contentCategoryQuery = new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = contentCategoryQuery.createCriteria();
        // 添加模糊查询条件
        if (contentCategory != null) {
            if (contentCategory.getName() != null && !"".equals(contentCategory.getName())) {
                criteria.andNameLike("%"+ contentCategory.getName() +"%");
            }
        }
        PageHelper.startPage(page, pageSize);
        Page<ContentCategory> contentCategories = (Page<ContentCategory>) contentCategoryDao.selectByExample(contentCategoryQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(contentCategories.getTotal());
        pageResult.setRows(contentCategories.getResult());
        return pageResult;
    }

    @Override
    public void saveContentCat(ContentCategory contentCategory) {
        contentCategoryDao.insertSelective(contentCategory);
    }

    @Override
    public void editContentCat(ContentCategory contentCategory) {
        contentCategoryDao.updateByPrimaryKeySelective(contentCategory);
    }

    @Override
    public void deleteContentCat(Long[] idx) {
        ContentCategoryQuery contentCategoryQuery = new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = contentCategoryQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        contentCategoryDao.deleteByExample(contentCategoryQuery);
    }

    @Override
    public List<ContentCategory> getAllCategory() {
        return contentCategoryDao.selectByExample(null);
    }
}
