package com.fmStore.core.service;

import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.good.Brand;

public interface BrandService {
    PageResult getBrandByExample(Brand brand, Integer pageSize, Integer page);

    Integer addBrand(Brand brand);

    Integer editBrand(Brand brand);

    Integer deleteBrand(Long[] idx);
}

