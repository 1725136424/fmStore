package com.fmStore.core.service;

import java.util.Map;

public interface CmsService {
    // 根据商品id获取商品数据
    Map<String, Object> getGoodsDate(Long goodsId);

    // 根据商品数据和id创建静态页面
    void createStaticPage(Long goodsId, Map<String, Object> goodsData);
}
