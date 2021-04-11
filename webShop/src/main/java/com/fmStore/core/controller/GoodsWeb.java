package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.GoodsEntity;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.good.Goods;
import com.fmStore.core.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsWeb {
    @Reference
    private GoodsService goodsService;

    // 保存商品
    @RequestMapping("/saveGoods")
    public Result saveGoods(@RequestBody GoodsEntity goodsEntity) {
        Result result = new Result();
        try {
            // 获取当前的用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsEntity.getGoods().setSellerId(username);
            goodsService.saveGood(goodsEntity);
            result.setMessage("保存成功");
            result.setIsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("保存失败");
            result.setIsSuccess(false);
        }
        return result;
    }

    // 修改商品
    @RequestMapping("/editGoods")
    public Result editGoods(@RequestBody GoodsEntity goodsEntity) {
        Result result = new Result();
        try {
            // 获取当前的用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsEntity.getGoods().setSellerId(username);
            goodsService.editGoods(goodsEntity);
            result.setMessage("修改成功");
            result.setIsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("修改失败");
            result.setIsSuccess(false);
        }
        return result;
    }

    // 删除商品
    @RequestMapping("/deleteCommitGoods")
    public Result deleteGoods(Long[] idx) {
        Result result = new Result();
        try {
            // 获取当前的用户
            goodsService.deleteGoods(idx);
            result.setMessage("删除成功");
            result.setIsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("删除失败");
            result.setIsSuccess(false);
        }
        return result;
    }

    // 查询商品
    @RequestMapping("/getCommitGoods")
    public PageResult getCommitGoods(@RequestBody Goods goods, Integer page, Integer pageSize) {
        // 获取当前的用户id
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(name);
        PageResult result = goodsService.getCommitGoods(goods, page, pageSize);
        return result;
    }

    // 获取商品实体
    @RequestMapping("/getGoods")
    public GoodsEntity getGoods(Long id) {
        GoodsEntity goods = goodsService.getGoods(id);
        return goods;
    }
}
