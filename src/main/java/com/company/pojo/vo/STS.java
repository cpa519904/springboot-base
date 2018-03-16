package com.company.pojo.vo;

import lombok.Data;

@Data
public class STS {
    private String AccessKeyId;
    private String accessKeySecret;
    private String SecurityToken;
    private long Expiration;
    /**
     * bucketName
     */
    private String bucketName;
    /**
     * oss endpoint
     */
    private String endpoint;
    /**
     * 文件访问host
     */
    private String host;
}
