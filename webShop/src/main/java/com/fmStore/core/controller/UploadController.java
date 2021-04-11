package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.image.Image;
import com.fmStore.core.service.ImageService;
import com.fmStore.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Reference
    private ImageService imageService;
    @Value("${IMAGE_URL}")
    private String imgUrl;

    @RequestMapping("/uploadImage")
    public Map<String, Long> uploadImage(MultipartFile file, String color) {
        HashMap<String, Long> map = new HashMap<>();
        // 上传文件
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:FastDFS/fdfs_client.conf");
            String path = fastDFSClient.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            String urlPath = imgUrl + path;
            Image image = new Image();
            image.setUrl(urlPath);
            image.setColor(color);
            Long id = imageService.saveImage(image);
            map.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("id", null);
        }
        return map;
    }

    @RequestMapping("/getImage")
    public Image getImage(Long id) {
        Image image = imageService.getImage(id);
        return image;
    }

    @RequestMapping("/deleteImage")
    public Result deleteImage(@RequestBody Image image) {
        Result result = new Result();
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:FastDFS/fdfs_client.conf");
            String url = image.getUrl().replace(imgUrl, "");
            Integer integer = fastDFSClient.delete_file(url);
            if (integer != 0) {
                throw new Exception("delete image fail");
            }
            // 同步数据库
            imageService.deleteImage(image.getId());
            result.setIsSuccess(true);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }

    @RequestMapping("/uploadImageUEditor")
    public Map<String, String> uploadImageUEditor(MultipartFile upfile) {
        Map<String, String> map = new HashMap<>();
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:FastDFS/fdfs_client.conf");
            String path = fastDFSClient.uploadFile(upfile.getBytes(), upfile.getOriginalFilename(), upfile.getSize());
            String url = imgUrl + path;
            // 保存数据库中
            Image image = new Image();
            image.setUrl(url);
            imageService.saveImage(image);
            map.put("url", url);
            map.put("state", "SUCCESS");
            map.put("original", upfile.getOriginalFilename());
            map.put("title", upfile.getOriginalFilename());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
