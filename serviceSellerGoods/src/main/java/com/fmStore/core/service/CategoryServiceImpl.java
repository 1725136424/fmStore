package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.item.ItemCatDao;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.good.Brand;
import com.fmStore.core.pojo.good.BrandQuery;
import com.fmStore.core.pojo.item.ItemCat;
import com.fmStore.core.pojo.item.ItemCatQuery;
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
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageResult getCategory(ItemCat itemCat, Integer pageSize, Integer page) {
        // 获取所有分类数据 保存至redis以便以后取出
        // 判断redis是否有数据
        if (!redisTemplate.hasKey(RedisConst.CATEGORY_LIST_REDIS)) {
            List<ItemCat> itemCats = itemCatDao.selectByExample(null);
            if (itemCats != null) {
                // 保存redis
                for (ItemCat cat : itemCats) {
                    redisTemplate.boundHashOps(RedisConst.CATEGORY_LIST_REDIS).put(cat.getName(), cat.getTypeId());
                }
            }
        }
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.setOrderByClause("id asc");
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        // 添加模糊查询条件
        if (itemCat != null) {
            if (itemCat.getName() != null && !"".equals(itemCat.getName())) {
                criteria.andNameLike("%"+ itemCat.getName() +"%");
            }
            if (itemCat.getParentId() != null && !"".equals(itemCat.getParentId())) {
                criteria.andParentIdEqualTo(itemCat.getParentId());
            }
        }

        if (pageSize != null && page != null) {
            PageHelper.startPage(page, pageSize);
        } else {
            PageHelper.startPage(1, 100);
        }

        Page<ItemCat> brands = (Page<ItemCat>) itemCatDao.selectByExample(itemCatQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(brands.getTotal());
        pageResult.setRows(brands.getResult());
        return pageResult;
    }

    @Override
    public Integer saveCategory(ItemCat itemCat) {
        int count = itemCatDao.insertSelective(itemCat);
        // 更新redis
        if (redisTemplate.hasKey(RedisConst.CATEGORY_LIST_REDIS)) {
            redisTemplate.delete(RedisConst.CATEGORY_LIST_REDIS);
        }
        return count;
    }

    @Override
    public Integer editCategory(ItemCat itemCat) {

        // 更新redis
        if (redisTemplate.hasKey(RedisConst.CATEGORY_LIST_REDIS)) {
            redisTemplate.delete(RedisConst.CATEGORY_LIST_REDIS);
        }

        int count = itemCatDao.updateByPrimaryKeySelective(itemCat);
        return count;
    }

    @Override
    public Integer deleteCategory(Long[] idx) {

        // 更新redis
        if (redisTemplate.hasKey(RedisConst.CATEGORY_LIST_REDIS)) {
            redisTemplate.delete(RedisConst.CATEGORY_LIST_REDIS);
        }

        ItemCatQuery itemCatQuery = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        int count = itemCatDao.deleteByExample(itemCatQuery);
        return count;
    }

    @Override
    public List<ItemCat> getAllCategory() {
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);


        return itemCats;
    }
}
