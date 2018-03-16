package com.company.pojo.vo;

import lombok.Data;

@Data
public class OSS {
    /**
     * 应用访问key
     */
    private String accessKeyId;
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
    /**
     * 文件上传路径
     */
    private String dir;
    /**
     * 生成密钥的有效时间
     */
    private long expire;
    /**
     * 用户表单上传的策略policy
     */
    private String policy;
    /**
     * 对policy签名的结果
     */
    private String signature;
}
