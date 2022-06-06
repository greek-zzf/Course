package com.greek.Course.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
public class HttpException extends RuntimeException {

    private int statusCode;

    private HttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public static HttpException notFound(String message) {
        return new HttpException(message, HttpStatus.NOT_FOUND.value());
    }

    public static HttpException forbidden(String message) {
        return new HttpException(message, HttpStatus.FORBIDDEN.value());
    }

    public static HttpException badRequest(String message) {
        return new HttpException(message, HttpStatus.BAD_REQUEST.value());
    }

    public static HttpException unauthorized(String message) {
        return new HttpException(message, HttpStatus.UNAUTHORIZED.value());
    }

    public static HttpException gone(String message) {
        return new HttpException(message, HttpStatus.GONE.value());
    }

    public int getStatusCode() {
        return statusCode;
    }
}
