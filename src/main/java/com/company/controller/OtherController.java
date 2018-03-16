package com.company.controller;

import com.company.pojo.vo.OSS;
import com.company.pojo.vo.enums.OssScene;
import com.company.pojo.vo.STS;
import com.company.service.IOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class OtherController {
    @Autowired
    private IOtherService otherService;

    @PostMapping("/getOssConfig")
    public OSS getOssConfig(@RequestBody OssScene ossScene) {
        return otherService.getOssConfig(ossScene);
    }

    @PostMapping("/getOssConfigForApp")
    public STS getOssConfigForApp(@RequestBody OssScene ossScene) {
        return otherService.getSTSToken(ossScene);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i<20; i++){
            executorService.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

    }
}
