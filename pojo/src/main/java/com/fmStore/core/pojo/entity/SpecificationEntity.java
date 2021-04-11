package com.fmStore.core.pojo.entity;

import com.fmStore.core.pojo.specification.Specification;
import com.fmStore.core.pojo.specification.SpecificationOption;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SpecificationEntity implements Serializable {
    private Specification specification;
    private List<SpecificationOption> specificationOptions;
}
