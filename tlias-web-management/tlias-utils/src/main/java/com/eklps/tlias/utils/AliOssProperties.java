package com.eklps.tlias.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss") // 核心注解
public class AliOssProperties {
    // 属性名必须和配置文件里的最后一段 key 保持一致 (Spring会自动转换驼峰)
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}