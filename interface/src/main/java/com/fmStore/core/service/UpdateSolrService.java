package com.fmStore.core.service;

import com.fmStore.core.pojo.item.Item;

import java.util.List;

public interface UpdateSolrService {
    void addSolr(List<Item> items);

    void deleteGoods(Long[] idx);
}
