package com.greek.Course.controller;

import cn.hutool.core.lang.UUID;
import com.greek.Course.model.AliOssConfig;
import com.greek.Course.service.AliOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VideoController {

    @Autowired
    private AliOssService aliOssService;
    @Autowired
    private

    @PostMapping("/course/{id}/video")
    AliOssConfig createCourse() {
        String uuid = UUID.randomUUID().toString();
        return aliOssService.getPolicyAndSign(uuid);
    }
}
