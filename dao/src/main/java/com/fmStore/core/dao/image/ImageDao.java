package com.fmStore.core.dao.image;

import com.fmStore.core.pojo.image.Image;
import com.fmStore.core.pojo.image.ImageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageDao {
    int countByExample(ImageQuery example);

    int deleteByExample(ImageQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Image record);

    int insertSelective(Image record);

    List<Image> selectByExample(ImageQuery example);

    Image selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Image record, @Param("example") ImageQuery example);

    int updateByExample(@Param("record") Image record, @Param("example") ImageQuery example);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);
}