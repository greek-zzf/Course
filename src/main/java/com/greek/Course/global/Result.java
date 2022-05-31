package com.greek.Course.global;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
public class Result<T> {

    T data;
    String msg;

    public Result() {
    }

    protected Result(T data, String msg) {
        this.data = data;
    }

    public static <T> Result of(T data, String msg) {
        return new Result<T>(data, msg);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(data, null);
    }

    public static <T> Result<T> failure(String msg) {
        return new Result<T>(null, msg);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }
}
