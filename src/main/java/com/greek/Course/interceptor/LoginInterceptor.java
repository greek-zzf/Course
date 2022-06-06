package com.greek.Course.interceptor;

import com.greek.Course.dao.SysUserRepository;
import com.greek.Course.global.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

/**
 * 用户登录拦截器，对用户操作进行拦截判断
 *
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final String LOGIN_COOKE_NAME = "JESSIONID";

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(LOGIN_COOKE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .flatMap(sysUserRepository::findByCookie)
                .ifPresent(UserContext::setUser);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clearUser();
    }
}
