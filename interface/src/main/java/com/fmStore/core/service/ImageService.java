package com.fmStore.core.service;


import com.fmStore.core.pojo.image.Image;

public interface ImageService {
    Long saveImage(Image image);

    Image getImage(Long id);

    Integer deleteImage(Long id);
}
