package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.specification.SpecificationDao;
import com.fmStore.core.dao.specification.SpecificationOptionDao;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.SpecificationEntity;
import com.fmStore.core.pojo.specification.Specification;
import com.fmStore.core.pojo.specification.SpecificationOption;
import com.fmStore.core.pojo.specification.SpecificationOptionQuery;
import com.fmStore.core.pojo.specification.SpecificationQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult getSpecByExample(Specification specification, Integer pageSize, Integer page) {
        SpecificationQuery specificationQuery = new SpecificationQuery();
        specificationQuery.setOrderByClause("id desc");
        if (specification != null) {
            SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();
            if (specification.getSpecName() != null && !"".equals(specification.getSpecName())) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }
        }
        if (pageSize != null && page != null) {
            PageHelper.startPage(page, pageSize);
        } else {
            PageHelper.startPage(1, 100);
        }
        Page<Specification> specifications = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(specifications.getTotal());
        pageResult.setRows(specifications.getResult());
        return pageResult;
    }

    @Override
    public Integer saveSpec(SpecificationEntity specificationEntity) {
        // 保存规格
        int count = specificationDao.insertSelective(specificationEntity.getSpecification());
        List<SpecificationOption> specificationOptions = specificationEntity.getSpecificationOptions();
        for (SpecificationOption item : specificationOptions) {
            item.setSpecId(specificationEntity.getSpecification().getId());
            specificationOptionDao.insertSelective(item);
        }
        return count;
    }

    @Override
    public List<SpecificationOption> getSpecOptions(Long id) {
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
        return specificationOptions;
    }

    @Override
    public Integer editSpec(SpecificationEntity specificationEntity) {
        // 删除规格选项
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdEqualTo(specificationEntity.getSpecification().getId());
        specificationOptionDao.deleteByExample(specificationOptionQuery);
        // 更新规格
        int count = specificationDao.updateByPrimaryKeySelective(specificationEntity.getSpecification());
        // 添加规格选项
        List<SpecificationOption> specificationOptions = specificationEntity.getSpecificationOptions();
        for (SpecificationOption item : specificationOptions) {
            // 设置外键
            item.setSpecId(specificationEntity.getSpecification().getId());
            // 添加
            specificationOptionDao.insertSelective(item);
        }
        return count;
    }

    @Override
    public Integer deleteSpec(Long[] idx) {
        // 删除对应的规格选项
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdIn(Arrays.asList(idx));
        int count1 = specificationOptionDao.deleteByExample(specificationOptionQuery);
        // 删除规格
        SpecificationQuery specificationQuery = new SpecificationQuery();
        SpecificationQuery.Criteria criteria1 = specificationQuery.createCriteria();
        criteria1.andIdIn(Arrays.asList(idx));
        int count2 = specificationDao.deleteByExample(specificationQuery);
        return count1 + count2;
    }
}
