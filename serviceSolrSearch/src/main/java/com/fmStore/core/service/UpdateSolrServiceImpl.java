package com.fmStore.core.service;
import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.pojo.good.Goods;
import com.fmStore.core.pojo.item.Item;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.Arrays;
import java.util.List;

@Service
public class UpdateSolrServiceImpl implements UpdateSolrService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void addSolr(List<Item> items) {
        // 获取审核通过的商品保存数
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    @Override
    public void deleteGoods(Long[] idx) {
        SimpleQuery simpleQuery = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(Arrays.asList(idx));
        simpleQuery.addCriteria(criteria);
        solrTemplate.delete(simpleQuery);
        solrTemplate.commit();
    }
}
