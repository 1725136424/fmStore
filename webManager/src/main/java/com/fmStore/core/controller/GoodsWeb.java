package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.GoodsEntity;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.good.Goods;
import com.fmStore.core.pojo.item.Item;
import com.fmStore.core.service.CmsService;
import com.fmStore.core.service.GoodsService;
import com.fmStore.core.service.UpdateSolrService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsWeb {
    @Reference
    private GoodsService goodsService;

    @Reference
    private UpdateSolrService updateSolrService;

    @Reference
    private CmsService cmsService;

    // 删除商品
    @RequestMapping("/deleteCommitGoodsRel")
    public Result deleteGoods(Long[] idx) {
        Result result = new Result();
        try {
            // 获取当前的用户
            goodsService.deleteGoodsRel(idx);
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

    // 审核通过商品
    @RequestMapping("/passCommitGoods")
    public Result passCommitGoods(Long[] idx) {
        Result result = new Result();
        try {
            // 获取当前的用户
            goodsService.passCommitGoods(idx);
            result.setMessage("操作成功");
            result.setIsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("操作失败");
            result.setIsSuccess(false);
        }
        return result;
    }

    // 驳回商品
    @RequestMapping("/rollbackGoods")
    public Result rollbackGoods(Long[] idx) {
        Result result = new Result();
        try {
            // 获取当前的用户
            goodsService.rollbackGoods(idx);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("操作失败");
            result.setIsSuccess(false);
        }
        return result;
    }

}
