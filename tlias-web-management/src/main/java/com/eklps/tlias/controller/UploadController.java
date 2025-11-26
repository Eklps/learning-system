package com.eklps.tlias.controller;

import com.eklps.tlias.pojo.Result;
import com.eklps.tlias.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    public AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws IOException{

        log.info("正在上传文件{}",image.getOriginalFilename());
        //以下是原本的本地存储的方法
//
//        //获取原始文件名
//        String originalFilename=image.getOriginalFilename();
//
//        //构造唯一文件名，防止文件被覆盖
//        String extname=originalFilename.substring(originalFilename.lastIndexOf("."));
//        String newFilename= UUID.randomUUID().toString()+extname;
//
//        //将文件保存到本地磁盘
//        image.transferTo(new File("F:/javaMain/firstCompleteProject/images/"+newFilename));
//
//        //返回图片访问的URL
//        //这里暂时假设通过一个后端映射来完成
//        String url="http://localhost:8080/images/"+newFilename;

        String url= aliOssUtil.upload(image);
        log.info("文件上传成功，访问阿里云URL{}",url);
        return Result.success(url);
    }
}
