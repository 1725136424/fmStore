package com.fmStore.core.pojo.entity;

import com.fmStore.core.pojo.order.OrderItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BuyerCart implements Serializable {
    private String sellerId;
    private String sellerName;
    private List<OrderItem> orderItemList;
}
