package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.seller.Seller;
import com.fmStore.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerWeb {
    @Reference
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping("/register")
    public Result register(@RequestBody Seller seller) {
        Result result = new Result();
        try{
            String encodePwd = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(encodePwd);
            sellerService.addSeller(seller);
            result.setIsSuccess(true);
            result.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("保存失败");
        }
        return result;
    }
}
