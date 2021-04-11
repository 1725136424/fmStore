package com.fmStore.core.service;

import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.seller.Seller;

public interface SellerService {
    Integer addSeller(Seller seller);

    PageResult getSellerByExample(Seller seller, Integer pageSize, Integer page);

    Integer editSeller(Seller seller);

    Seller findSellerByUsername(String username);
}
