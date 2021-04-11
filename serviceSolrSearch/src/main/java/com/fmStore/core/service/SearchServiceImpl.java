package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fmStore.core.pojo.item.Item;
import com.fmStore.core.utils.RedisConst;
import org.opensaml.xml.signature.J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public HashMap<String, Object> getItem(Map<String, Object> map) {
        HashMap<String, Object> heightLightMap = getHeightLight(map);

        // 查询对应的分类数据
        ArrayList<String> groupCategoryList = findGroupCategoryList(map);

        // 添加对应的分类数据
        heightLightMap.put("categoryList", groupCategoryList);

        // 判断传入是数据是否包含分类
        String category = String.valueOf(map.get("category"));
        Map<String, Object> specListAndBrandList = null;
        if (category != null && !"null".equals(category) && !"".equals(category)) {
            specListAndBrandList = getSpecListAndBrandList(category);
        } else {
            // 取出第一个
            specListAndBrandList = getSpecListAndBrandList(groupCategoryList.get(0));
        }
        // 添加到返回的集合中
        heightLightMap.put("specAndBrand", specListAndBrandList);


        return heightLightMap;
    }

    private ArrayList<String> findGroupCategoryList(Map<String, Object> map) {
        // 获取查询字段 查询所有集合
        String keywords = String.valueOf(map.get("keywords"));
        // 查询对应的品牌数据
        SimpleQuery simpleQuery = new SimpleQuery();
        Criteria item_keywords = new Criteria("item_keywords").is(keywords);
        simpleQuery.addCriteria(item_keywords);
        // 创建分组字段
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        simpleQuery.setGroupOptions(groupOptions);
        // 查询分组字段
        GroupPage<Item> items = solrTemplate.queryForGroupPage(simpleQuery, Item.class);
        // 获取结果集中分类的集合
        GroupResult<Item> item_category = items.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = item_category.getGroupEntries();
        ArrayList<String> categoryList = new ArrayList<>();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();
            categoryList.add(groupValue);
        }
        return categoryList;
    }

    private HashMap<String, Object> getHeightLight(Map<String, Object> map) {
        // 通过solr查询对应的数据
        String keywords = String.valueOf(map.get("keywords"));
        Integer page = Integer.parseInt(String.valueOf(map.get("page")));
        Integer pageSize = Integer.parseInt(String.valueOf(map.get("pageSize")));

        String category = String.valueOf(map.get("category"));
        String brand = String.valueOf(map.get("brand"));
        String spec = String.valueOf(map.get("spec"));
        String price = String.valueOf(map.get("price"));



//        SimpleQuery query = new SimpleQuery();
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        // 添加高亮选项
        HighlightOptions highlightOptions = new HighlightOptions();
        // 添加高亮的内容
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<span style='color: red'>");
        highlightOptions.setSimplePostfix("</span>");

        // 添加高亮选项至查询条件中
        query.setHighlightOptions(highlightOptions);


        // 添加分类条件
        if (category != null && !"null".equals(category) && !"".equals(category)) {
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_category").is(category);
            filterQuery.addCriteria(criteria);
            query.addFilterQuery(filterQuery);
        }

        // 品牌
        if (brand != null && !"null".equals(brand) && !"".equals(brand)) {
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_brand").is(brand);
            filterQuery.addCriteria(criteria);
            query.addFilterQuery(filterQuery);
        }

        // 价格
        if (price != null && !"null".equals(price) && !"".equals(price)) {
            String[] split = price.split("-");
            if ("*".equals(split[1])) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria item_price = new Criteria("item_price").greaterThanEqual(split[0]);
                filterQuery.addCriteria(item_price);
                query.addFilterQuery(filterQuery);
            } else {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria item_price = new Criteria("item_price").greaterThanEqual(split[0]);
                Criteria item_price1 = new Criteria("item_price").lessThanEqual(split[1]);
                filterQuery.addCriteria(item_price);
                filterQuery.addCriteria(item_price1);
                query.addFilterQuery(filterQuery);
            }
        }

        // 规格
        if (spec != null && !"null".equals(spec) && !"".equals(spec)) {
            Map specMap = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> set = specMap.entrySet();
            for (Map.Entry<String, String> res : set) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_spec_" + res.getKey()).is(res.getValue());
                filterQuery.addCriteria(criteria);
                query.addFilterQuery(filterQuery);
            }
        }

        // 查询中必须有一个条件
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        // 添加条件
        query.addCriteria(criteria);

        // 设置分页查询
        if (page == null || page < 1) {
            page = 1;
        }
        // 设置开始
        query.setOffset((page - 1) * pageSize);
        // 设置查询记录数
        query.setRows(pageSize);

        // 查询记录返回结果
//        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);
        List<HighlightEntry<Item>> highlighted = items.getHighlighted();
        ArrayList<Item> items1 = new ArrayList<>();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            // 获取没有替换的内容
            Item entity = itemHighlightEntry.getEntity();
            // 获取高亮文字
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if (highlights != null && highlights.size() > 0) {
                String highLightWord = highlights.get(0).getSnipplets().get(0);
                entity.setTitle(highLightWord);
            }
            items1.add(entity);
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("rows", items1);
        hashMap.put("total", items.getTotalElements());
        hashMap.put("totalPage", items.getTotalPages());
        return hashMap;
    }

    private Map<String, Object> getSpecListAndBrandList(String category) {
        // 根据分类名称查询对应的模板id -> 在redis中
        Long templateId = (Long) redisTemplate.boundHashOps(RedisConst.CATEGORY_LIST_REDIS)
                .get(category);

        // 查询对应的品牌和规格数据
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps(RedisConst.BRAND_LIST_REDIS)
                .get(templateId);

        Map<String, Object> specList = (Map<String, Object>) redisTemplate.boundHashOps(RedisConst.SPEC_LIST_REDIS)
                .get(templateId);

//        List<Map> brandList = JSON.parseArray(brandStr, Map.class);
//        List<Map> specList = JSON.parseArray(specStr, Map.class);
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("brandList", brandList);
        stringObjectHashMap.put("specList", specList);

        return stringObjectHashMap;
    }
}
