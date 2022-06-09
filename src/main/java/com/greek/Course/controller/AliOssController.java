package com.greek.Course.controller;

import com.greek.Course.model.AliOssConfig;
import com.greek.Course.service.AliOssServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliOssController {

    private AliOssServer aliOssServer;

    @Autowired
    AliOssController(AliOssServer aliOssServer) {
        this.aliOssServer = aliOssServer;
    }

    @GetMapping("/policy")
    public AliOssConfig getPolicyAndSign() {
        return aliOssServer.getPolicyAndSign();
    }

}
