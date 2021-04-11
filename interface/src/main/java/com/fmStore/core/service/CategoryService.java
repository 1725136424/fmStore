package com.fmStore.core.service;

import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.item.ItemCat;

import java.util.List;

public interface CategoryService {
    PageResult getCategory(ItemCat itemCat, Integer pageSize, Integer page);

    Integer saveCategory(ItemCat itemCat);

    Integer editCategory(ItemCat itemCat);

    Integer deleteCategory(Long[] idx);

    List<ItemCat> getAllCategory();
}
