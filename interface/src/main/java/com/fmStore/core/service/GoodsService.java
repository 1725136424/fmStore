package com.fmStore.core.service;

import com.fmStore.core.pojo.entity.GoodsEntity;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.good.Goods;
import com.fmStore.core.pojo.item.Item;

import java.util.List;


public interface GoodsService {
    void saveGood(GoodsEntity goodEntity);

    PageResult getCommitGoods(Goods goods, Integer page, Integer pageSize);

    GoodsEntity getGoods(Long id);

    void editGoods(GoodsEntity goodsEntity);

    void deleteGoods(Long[] idx);

    void deleteGoodsRel(Long[] idx);

    void passCommitGoods(Long[] idx);

    void rollbackGoods(Long[] idx);

    List<Item> findCommitGoodsById(Long[] idx);
}
