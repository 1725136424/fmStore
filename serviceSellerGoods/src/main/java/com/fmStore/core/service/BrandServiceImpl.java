package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.good.BrandDao;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.good.Brand;
import com.fmStore.core.pojo.good.BrandQuery;
import com.fmStore.core.utils.RedisConst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService{
    @Autowired
    private BrandDao brandDao;
    @Override
    public PageResult getBrandByExample(Brand brand, Integer pageSize, Integer page) {
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setOrderByClause("id desc");
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        // 添加模糊查询条件
        if (brand != null) {
            if (brand.getName() != null && !"".equals(brand.getName())) {
                criteria.andNameLike("%"+ brand.getName() +"%");
            }
            if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar())) {
                criteria.andFirstCharLike("%"+ brand.getFirstChar() +"%");
            }
        }

        if (pageSize != null && page != null) {
            PageHelper.startPage(page, pageSize);
        } else {
            PageHelper.startPage(1, 100);
        }

        Page<Brand> brands = (Page<Brand>) brandDao.selectByExample(brandQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(brands.getTotal());
        pageResult.setRows(brands.getResult());
        return pageResult;
    }

    @Override
    public Integer addBrand(Brand brand) {
        int count = brandDao.insertSelective(brand);
        return count;
    }

    @Override
    public Integer editBrand(Brand brand) {
        int count = brandDao.updateByPrimaryKeySelective(brand);
        return count;
    }

    @Override
    public Integer deleteBrand(Long[] idx) {
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        int count = brandDao.deleteByExample(brandQuery);
        return count;
    }
}
