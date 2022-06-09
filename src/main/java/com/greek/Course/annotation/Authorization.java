package com.greek.Course.annotation;

import java.lang.annotation.*;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/1
 * <p>
 * 表示指定的方法需要进行权限校验
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
}

