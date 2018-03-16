package com.company.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.company.common.exception.SystemException;
import com.company.common.tools.Time;
import com.company.common.exception.ExceptionCode;
import com.company.pojo.vo.OSS;
import com.company.pojo.vo.enums.OssScene;
import com.company.pojo.vo.STS;
import com.company.service.IOtherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class OtherServiceImpl implements IOtherService{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${oss.host}")
    private String host;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${sts.accessKeyId}")
    private String stsAccessKeyId;

    @Value("${sts.accessKeySecret}")
    private String stsAccessKeySecret;

    @Value("${sts.roleArn}")
    private String roleArn;

    private static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    private static final String STS_API_VERSION = "2015-04-01";
    private static final String STS_POLICE = "{\"Statement\": [{\"Action\": [\"oss:PutObject\"],\"Effect\": \"Allow\",\"Resource\": [\"acs:oss:*:*:%s/%s/*\"]}],\"Version\": \"1\"}\n";

    public OSS getOssConfig(OssScene ossScene) {
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        String filePath = ossScene.getFilePath();

        long expireEndTime = System.currentTimeMillis() + 30 * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions conditions = new PolicyConditions();
        conditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, filePath);
        String postPolicy = client.generatePostPolicy(expiration, conditions);

        String encodedPolicy;
        try {
            byte[] binaryData = postPolicy.getBytes("utf-8");
            encodedPolicy = BinaryUtil.toBase64String(binaryData);
        } catch (UnsupportedEncodingException e) {
            logger.error("getOssConfig error", e);
            throw new SystemException(ExceptionCode.SYSTEM_ERROR.getCode(), "getOssConfig error");
        }

        String postSignature = client.calculatePostSignature(postPolicy);

        OSS config = new OSS();
        config.setAccessKeyId(accessKeyId);
        config.setBucketName(bucketName);
        config.setEndpoint(endpoint);
        config.setHost(host);
        config.setDir(filePath);
        config.setExpire(expireEndTime);
        config.setPolicy(encodedPolicy);
        config.setSignature(postSignature);

        return config;
    }

    public STS getSTSToken(OssScene ossScene) {
        long durationSeconds = 30 * 60;

        try {
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, stsAccessKeyId, stsAccessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(ProtocolType.HTTPS);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(ossScene.getFilePath());
            request.setPolicy(String.format(STS_POLICE, bucketName, ossScene.getFilePath()));
            request.setDurationSeconds(durationSeconds);

            AssumeRoleResponse response = client.getAcsResponse(request);

            STS sts = new STS();
            sts.setAccessKeyId(response.getCredentials().getAccessKeyId());
            sts.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            sts.setSecurityToken(response.getCredentials().getSecurityToken());
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sts.setExpiration(Time.toTimestamp(LocalDateTime.parse(response.getCredentials().getExpiration(), df)));
            sts.setBucketName(bucketName);
            sts.setEndpoint(endpoint);
            sts.setHost(host);
            return sts;
        } catch (Exception e) {
            logger.error("AssumeRole error", e);
            throw new SystemException(ExceptionCode.SYSTEM_ERROR.getCode(), "AssumeRole error");
        }
    }
}
