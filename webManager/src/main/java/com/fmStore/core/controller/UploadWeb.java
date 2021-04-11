package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.image.Image;
import com.fmStore.core.service.ImageService;
import com.fmStore.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadWeb {
    @Reference
    private ImageService imageService;
    @Value("${IMAGE_URL}")
    private String rootPath;
    @RequestMapping("/uploadImage")
    public Map uploadImage(MultipartFile file) {
        Map<String, Object> objectObjectMap = new HashMap<>();
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:FastDFS/fdfs_client.conf");
            String path = fastDFSClient.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            String picPath = rootPath + path;
            // 保存图片
            Image image = new Image();
            image.setUrl(picPath);
            Long id = imageService.saveImage(image);
            objectObjectMap.put("id", id);
            objectObjectMap.put("url", picPath);
        } catch (Exception e) {
            e.printStackTrace();
            objectObjectMap.put("id", null);
            objectObjectMap.put("url", null);
        }
        return objectObjectMap;

    }
}
