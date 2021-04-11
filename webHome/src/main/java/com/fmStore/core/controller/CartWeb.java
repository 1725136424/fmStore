package com.fmStore.core.controller;

import com.fmStore.core.pojo.entity.BuyerCart;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.order.OrderItem;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartWeb {

    @RequestMapping("/addGoodsToCartList")
    /*允许跨域访问*/
    @CrossOrigin(origins = "http://127.0.0.1:8086", allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        Result result = new Result();
        try {
            result.setMessage("添加成功");
            result.setIsSuccess(true);
        } catch (Exception e) {
            result.setMessage("添加失败");
            result.setIsSuccess(false);
        }
        return result;
    }

    @RequestMapping("/getCartList")
    public List<BuyerCart> getCartList() {
        /*模拟数据*/
        List<BuyerCart> cartLists = new ArrayList<>();
        BuyerCart buyerCart = new BuyerCart();
        buyerCart.setSellerId("gaowei");
        buyerCart.setSellerName("高伟");
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setTitle("aaaaa 移动3G 16G");
        orderItem1.setGoodsId(new Long(1234));
        orderItem1.setPrice(new BigDecimal(222.00));
        orderItem1.setPicPath("http://192.168.0.111/group1/M00/00/00/wKgAb16xJkuAA6CYAABdMIT9zVA937.jpg");
        orderItem1.setSellerId("gaowei");
        orderItem1.setNum(2);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setTitle("aaaaa 移动3G 16G222");
        orderItem2.setGoodsId(new Long(12344));
        orderItem2.setPrice(new BigDecimal(122.00));
        orderItem2.setPicPath("http://192.168.0.111/group1/M00/00/01/wKgAb161JgaAIiw9AACJ6bjrHfM890.jpg");
        orderItem2.setSellerId("gaowei");
        orderItem2.setNum(1);

        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);
        buyerCart.setOrderItemList(orderItemList);

        BuyerCart buyerCart2 = new BuyerCart();
        buyerCart2.setSellerId("gaowei2");
        buyerCart2.setSellerName("高伟2");
        OrderItem orderItem3 = new OrderItem();
        orderItem3.setTitle("aaaaa 移动3G 16G1");
        orderItem3.setGoodsId(new Long(1234));
        orderItem3.setPrice(new BigDecimal(232.00));
        orderItem3.setPicPath("http://192.168.0.111/group1/M00/00/00/wKgAb16xJkuAA6CYAABdMIT9zVA937.jpg");
        orderItem3.setSellerId("gaowei");
        orderItem3.setNum(2);
        ArrayList<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(orderItem3);
        buyerCart2.setOrderItemList(orderItemList2);

        cartLists.add(buyerCart);
        cartLists.add(buyerCart2);
        return cartLists;
    }
}
