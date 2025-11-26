package com.eklps.tlias.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component // 交给 IOC 容器管理
public class AliOssUtil {

    // 从 application.properties 读取配置
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 实现上传图片到 OSS
     */
    public String upload(MultipartFile file) throws IOException {
        // 1. 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 2. 避免文件覆盖，构造唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 3. 上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        // 4. 获取文件访问路径
        // 格式: https://bucketName.endpoint/fileName
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 5. 关闭 ossClient
        ossClient.shutdown();

        return url; // 把上传到阿里云的图片路径返回
    }
}