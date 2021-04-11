package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.image.ImageDao;
import com.fmStore.core.pojo.image.Image;
import com.fmStore.core.pojo.image.ImageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageDao imageDao;
    @Override
    public Long saveImage(Image image) {
        imageDao.insertSelective(image);
        return image.getId();
    }

    @Override
    public Image getImage(Long id) {
        ImageQuery imageQuery = new ImageQuery();
        ImageQuery.Criteria criteria = imageQuery.createCriteria();
        criteria.andIdEqualTo(id);
        List<Image> images = imageDao.selectByExample(imageQuery);
        return images.get(0);
    }

    @Override
    public Integer deleteImage(Long id) {
        int count = imageDao.deleteByPrimaryKey(id);
        return count;
    }
}
