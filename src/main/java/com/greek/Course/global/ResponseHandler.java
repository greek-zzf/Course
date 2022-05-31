package com.greek.Course.global;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
@RestControllerAdvice(basePackages = "com.greek.shop.controller")
public class ResponseHandler implements ResponseBodyAdvice {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return needConvert(body) ? Result.success(body) : body;
    }

    private boolean needConvert(Object object) {
        return !(object instanceof Result);
    }
}

