package com.greek.Course.interceptor;

import com.greek.Course.dao.SessionDao;
import com.greek.Course.global.UserContext;
import com.greek.Course.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 用户登录拦截器，对用户操作进行拦截判断
 *
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOGIN_COOKE_NAME = "JESSIONID";

    private SessionDao sessionDao;

    public LoginInterceptor(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        getCookie(request)
                .flatMap(sessionDao::findByCookie)
                .map(Session::getUser)
                .ifPresent(UserContext::setCurrentUser);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clearUser();
    }


    public static Optional<String> getCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        Cookie[] cookies = request.getCookies();

        return Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(LOGIN_COOKE_NAME))
                .map(Cookie::getValue)
                .findFirst();
    }
}
