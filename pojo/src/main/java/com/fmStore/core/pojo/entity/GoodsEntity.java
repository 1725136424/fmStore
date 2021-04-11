package com.fmStore.core.pojo.entity;

import com.fmStore.core.pojo.good.Goods;
import com.fmStore.core.pojo.good.GoodsDesc;
import com.fmStore.core.pojo.item.Item;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsEntity implements Serializable {
    private Goods goods;
    private GoodsDesc goodsDesc;
    private List<Item> items;
}
