package com.greek.Course.global;

import com.greek.Course.exception.HttpException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(HttpException.class)
    public Result httpExceptionHandler(HttpException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatusCode());
        return Result.failure(ex.getMessage());
    }
}
