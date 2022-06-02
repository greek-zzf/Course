package com.greek.Course.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/1
 * <p>
 * 对标注的 @Authorization 进行权限校验
 */
@Aspect
@Order(1)
public class AuthorizationAop {

    @Pointcut("@annotation(com.greek.Course.annotation.Authorization)")
    public void controller() {
    }

    @Around(value = "controller()")
    public Object authorization(ProceedingJoinPoint proceed) throws Throwable {
        // 获取当前用户信息，以及对应的权限

        return proceed.proceed();
    }


}
