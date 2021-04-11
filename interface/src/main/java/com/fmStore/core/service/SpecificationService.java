package com.fmStore.core.service;

import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.entity.SpecificationEntity;
import com.fmStore.core.pojo.specification.Specification;
import com.fmStore.core.pojo.specification.SpecificationOption;

import java.util.List;

public interface SpecificationService {
    PageResult getSpecByExample(Specification specification, Integer pageSize, Integer page);

    Integer saveSpec(SpecificationEntity specificationEntity);

    List<SpecificationOption> getSpecOptions(Long id);

    Integer editSpec(SpecificationEntity specificationEntity);

    Integer deleteSpec(Long[] idx);
}
