package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fmStore.core.dao.specification.SpecificationOptionDao;
import com.fmStore.core.dao.template.TypeTemplateDao;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.item.ItemCat;
import com.fmStore.core.pojo.specification.SpecificationOption;
import com.fmStore.core.pojo.specification.SpecificationOptionQuery;
import com.fmStore.core.pojo.template.TypeTemplate;
import com.fmStore.core.pojo.template.TypeTemplateQuery;
import com.fmStore.core.utils.RedisConst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TypeTemplateDao typeTemplateDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageResult getTemplateByExample(TypeTemplate typeTemplate, Integer pageSize, Integer page) {

        // 缓存品牌
        if (!redisTemplate.hasKey(RedisConst.BRAND_LIST_REDIS)) {
            List<TypeTemplate> typeTemplates = typeTemplateDao.selectByExample(null);
            if (typeTemplates != null) {
                for (TypeTemplate template : typeTemplates) {
                    List<Map> brandMap = JSON.parseArray(template.getBrandIds(), Map.class);
                    redisTemplate.boundHashOps(RedisConst.BRAND_LIST_REDIS)
                            .put(template.getId(), brandMap);

                    // 通过对应的的模板查询规格和规格选项集合
                    Map specAndSpecOption = getSpecAndSpecOption(template.getId());
                    redisTemplate.boundHashOps(RedisConst.SPEC_LIST_REDIS)
                            .put(template.getId(), specAndSpecOption);
                }
            }
        }

        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        typeTemplateQuery.setOrderByClause("id desc");
        if (typeTemplate != null) {
            TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
            if (typeTemplate.getName() != null && !"".equals(typeTemplate.getName())) {
                criteria.andNameLike("%" + typeTemplate.getName()+ "%");
            }
        }
        PageHelper.startPage(page, pageSize);
        Page<TypeTemplate> typeTemplates = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(typeTemplates.getTotal());
        pageResult.setRows(typeTemplates.getResult());
        return pageResult;
    }

    @Override
    public Integer saveTemplate(TypeTemplate typeTemplate) {
        int count = typeTemplateDao.insertSelective(typeTemplate);

        // 更新redis
        if (redisTemplate.hasKey(RedisConst.BRAND_LIST_REDIS)  || redisTemplate.hasKey(RedisConst.SPEC_LIST_REDIS)) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(RedisConst.BRAND_LIST_REDIS);
            strings.add(RedisConst.SPEC_LIST_REDIS);
            redisTemplate.delete(strings);
        }
        return count;
    }

    @Override
    public Integer editTemplate(TypeTemplate typeTemplate) {

        // 更新redis
        if (redisTemplate.hasKey(RedisConst.BRAND_LIST_REDIS)  || redisTemplate.hasKey(RedisConst.SPEC_LIST_REDIS)) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(RedisConst.BRAND_LIST_REDIS);
            strings.add(RedisConst.SPEC_LIST_REDIS);
            redisTemplate.delete(strings);
        }


        int count = typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
        return count;
    }

    @Override
    public Integer deleteTemplate(Long[] idx) {

        // 更新redis
        if (redisTemplate.hasKey(RedisConst.BRAND_LIST_REDIS) || redisTemplate.hasKey(RedisConst.SPEC_LIST_REDIS)) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(RedisConst.BRAND_LIST_REDIS);
            strings.add(RedisConst.SPEC_LIST_REDIS);
            redisTemplate.delete(strings);
        }

        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        int count = typeTemplateDao.deleteByExample(typeTemplateQuery);
        return count;
    }

    @Override
    public Map getSpecAndSpecOption(Long id) {
        // 获取当前模板
        TypeTemplate curTemplate = typeTemplateDao.selectByPrimaryKey(id);
        if (curTemplate == null) return null;
        // 获取单曲模板的规格
        String specString = curTemplate.getSpecIds();
        // String ---> Array
        List<Map> specArray = JSON.parseArray(specString, Map.class);
        // 利用当前的规格 查找出当前的规格选项
        for (Map item: specArray) {
            Object parseId = item.get("id");
            // Object --> String --> Long
            Long specId = Long.parseLong(String.valueOf(parseId));
            // 查询对应的规格选项
            SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
            criteria.andSpecIdEqualTo(specId);
            List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
            item.put("specificationOptions", specificationOptions);
        }
        // 获取模板对应的品牌信息
        String brandStr = curTemplate.getBrandIds();
        List<Map> brandList = JSON.parseArray(brandStr, Map.class);

        Map<String, List<Map>> stringListHashMap = new HashMap<>();
        stringListHashMap.put("specAndSpecOption", specArray);
        stringListHashMap.put("brand", brandList);
        return stringListHashMap;
    }

}
