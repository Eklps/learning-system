package com.eklps.tlias.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
//import com.eklps.tlias.pojo.AliOssProperties; // 1. 导入了我们新建的配置类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component // 2. 声明这是一个 Bean，交给 Spring 管理
public class AliOssUtil {

    // 3. 核心改变：直接注入配置对象，而不是一个个注入字符串
    @Autowired
    private AliOssProperties aliOssProperties;

    /**
     * 文件上传核心方法
     */
    public String upload(MultipartFile file) throws IOException {
        // --- A. 准备阶段 ---
        // 获取文件的输入流（我们要传的内容）
        InputStream inputStream = file.getInputStream();
        // 获取原始文件名 (例如: "avatar.jpg")
        String originalFilename = file.getOriginalFilename();
        // 生成唯一文件名 (UUID + 后缀)，防止文件名冲突覆盖
        // 结果例如: "d4e8a1... .jpg"
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // --- B. 读取配置阶段 (优雅之处) ---
        // 我们不再直接用 @Value 读取，而是找 aliOssProperties 这个“管家”要数据
        String endpoint = aliOssProperties.getEndpoint();
        String accessKeyId = aliOssProperties.getAccessKeyId();
        String accessKeySecret = aliOssProperties.getAccessKeySecret();
        String bucketName = aliOssProperties.getBucketName();

        // --- C. 上传阶段 ---
        // 创建 OSS 客户端 (这是阿里云 SDK 提供的入口)
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 执行上传：把流 (inputStream) 传到指定仓库 (bucketName) 的指定位置 (fileName)
        ossClient.putObject(bucketName, fileName, inputStream);

        // --- D. 收尾阶段 ---
        // 拼接图片访问路径
        // 逻辑：协议头(https://) + Bucket名 + "." + 区域节点 + "/" + 文件名
        // 最终效果: https://tlias-name.oss-cn-hangzhou.aliyuncs.com/d4e8...jpg
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 关闭客户端，释放连接资源 (重要！)
        ossClient.shutdown();

        // 返回 URL 给调用者 (Controller)
        return url;
    }
}