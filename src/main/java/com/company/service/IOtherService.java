package com.company.service;

import com.company.pojo.vo.enums.OssScene;
import com.company.pojo.vo.OSS;
import com.company.pojo.vo.STS;

public interface IOtherService {
    public OSS getOssConfig(OssScene ossScene);
    public STS getSTSToken(OssScene ossScene);
}
