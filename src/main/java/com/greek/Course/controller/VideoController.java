package com.greek.Course.controller;

import com.greek.Course.model.AliOssConfig;
import com.greek.Course.service.AliOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VideoController {

    @Autowired
    private AliOssService aliOssService;


    @GetMapping("/video/token")
    public AliOssConfig createCourse() {
        return aliOssService.getPolicyAndSign();
    }

    @GetMapping("/video/{id}")
    public String getVideoUrl(@PathVariable("id") Integer videoId) {
        return aliOssService.getVideoUrl(videoId);
    }


}
