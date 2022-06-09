package com.greek.Course.aspect;

import com.greek.Course.exception.HttpException;
import com.greek.Course.global.UserContext;
import com.greek.Course.model.RoleEnum;
import com.greek.Course.model.SysUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/1
 * <p>
 * 对标注的 @Authorization 进行权限校验
 */
@Aspect
@Order(1)
public class AuthorizationAop {


    @Around("@annotation(com.greek.Course.annotation.Admin)")
    public Object authorization(ProceedingJoinPoint proceed) throws Throwable {
        SysUser currentUser = UserContext.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            throw HttpException.unauthorized("用户没有登录！");
        }

        if (!isAdmin(currentUser)) {
            throw HttpException.forbidden("用户没有权限！");
        }

        return proceed.proceed();
    }

    private boolean isAdmin(SysUser currentUser) {
        return currentUser.getRoles()
                .stream()
                .anyMatch(role -> RoleEnum.ADMIN.name().equalsIgnoreCase(role.getName()));
    }


}

