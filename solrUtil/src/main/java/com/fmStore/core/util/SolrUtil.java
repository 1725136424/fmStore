package com.fmStore.core.util;


import com.alibaba.fastjson.JSON;
import com.fmStore.core.dao.item.ItemDao;
import com.fmStore.core.pojo.item.Item;
import com.fmStore.core.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("solr")
public class SolrUtil {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private ItemDao itemDao;

    public void importItemDataToSolr() {
        // 查询所有已经审核过的数据
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andStatusEqualTo("2");
        List<Item> items = itemDao.selectByExample(itemQuery);

        System.out.println(items);

        // 处理动态规格数据
        if (items != null) {
            for (Item item : items) {
                String specStr = item.getSpec();
                Map specObj = JSON.parseObject(specStr, Map.class);
                item.setSpecMap(specObj);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    public void deleteAll() {
        SimpleQuery simpleQuery = new SimpleQuery("*:*");
        solrTemplate.delete(simpleQuery);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        // 执行方法
        ClassPathXmlApplicationContext build = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) build.getBean("solr");
        solrUtil.deleteAll();
    }
}
