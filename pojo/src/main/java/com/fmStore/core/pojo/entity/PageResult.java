package com.fmStore.core.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PageResult implements Serializable {
    private Long total;
    private List rows;
}
