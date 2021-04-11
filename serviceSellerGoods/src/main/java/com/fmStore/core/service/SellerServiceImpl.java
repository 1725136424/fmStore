package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.seller.SellerDao;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.seller.Seller;
import com.fmStore.core.pojo.seller.SellerQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;
    @Override
    public Integer addSeller(Seller seller) {
        seller.setCreateTime(new Date());
        seller.setStatus("0");
        int count = sellerDao.insertSelective(seller);
        return count;
    }

    @Override
    public PageResult getSellerByExample(Seller seller, Integer pageSize, Integer page) {
        SellerQuery sellerQuery = new SellerQuery();
        sellerQuery.setOrderByClause("create_time desc");
        SellerQuery.Criteria criteria = sellerQuery.createCriteria();
        // 添加模糊查询条件
        if (seller != null) {
            if (seller.getName() != null && !"".equals(seller.getName())) {
                criteria.andNameLike("%"+ seller.getName() +"%");
            }
            if (seller.getNickName() != null && !"".equals(seller.getNickName())) {
                criteria.andNickNameLike("%"+ seller.getNickName() +"%");
            }
            if (seller.getStatus() != null && !"".equals(seller.getStatus())) {
                criteria.andStatusEqualTo(seller.getStatus());
            }
        }

        if (pageSize != null && page != null) {
            PageHelper.startPage(page, pageSize);
        } else {
            PageHelper.startPage(1, 100);
        }

        Page<Seller> brands = (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(brands.getTotal());
        pageResult.setRows(brands.getResult());
        return pageResult;
    }

    @Override
    public Integer editSeller(Seller seller) {
        int count = sellerDao.updateByPrimaryKeySelective(seller);
        return count;
    }

    @Override
    public Seller findSellerByUsername(String username) {
        Seller seller = sellerDao.selectByPrimaryKey(username);
        return seller;
    }

}
