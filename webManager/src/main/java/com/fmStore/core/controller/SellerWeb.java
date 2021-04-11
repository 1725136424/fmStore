package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.seller.Seller;
import com.fmStore.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/seller")
@RestController
public class SellerWeb {
    @Reference
    private SellerService sellerService;

    @RequestMapping("/getSellerByExample")
    public PageResult getSellerByExample(@RequestBody Seller seller, Integer pageSize, Integer page) {
        PageResult result = sellerService.getSellerByExample(seller, pageSize, page);
        return result;
    }

    @RequestMapping("/editSeller")
    public Result editSeller(@RequestBody Seller seller) {
        Result result = new Result();
        try {
            sellerService.editSeller(seller);
            result.setIsSuccess(true);
            result.setMessage("审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(true);
            result.setMessage("审核未通过");
        }
        return result;

    }
}
