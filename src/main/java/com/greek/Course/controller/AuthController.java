package com.greek.Course.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.greek.Course.dao.SessionDao;
import com.greek.Course.exception.HttpException;
import com.greek.Course.global.UserContext;
import com.greek.Course.model.Session;
import com.greek.Course.model.SysUser;
import com.greek.Course.model.vo.SysUserVo;
import com.greek.Course.model.vo.UsernameAndPassword;
import com.greek.Course.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

import static com.greek.Course.interceptor.LoginInterceptor.LOGIN_COOKE_NAME;
import static com.greek.Course.interceptor.LoginInterceptor.getCookie;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private SysUserService sysUserService;
    private SessionDao sessionDao;

    @Autowired
    AuthController(SysUserService sysUserService, SessionDao sessionDao) {
        this.sysUserService = sysUserService;
        this.sessionDao = sessionDao;
    }


    /**
     * @api {get} /api/v1/session 检查登录状态
     * @apiName 检查登录状态
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiParamExample Request-Example:
     * GET /api/v1/auth
     * @apiSuccess {User} user 用户信息
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "user": {
     * "id": 123,
     * "username": "Alice"
     * }
     * }
     * @apiError 401 Unauthorized 若用户未登录
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    @GetMapping("/session")
    public Session currentUser() {
        SysUser currentUser = UserContext.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            throw HttpException.unauthorized("Unauthorized");
        } else {
            Session session = new Session();
            session.setUser(currentUser);
            return session;
        }
    }


    /**
     * @api {delete} /api/v1/session 登出
     * @apiName 登出
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiParamExample Request-Example:
     * DELETE /api/v1/session
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 204 No Content
     * @apiError 401 Unauthorized 若用户未登录
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    @DeleteMapping("/session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SysUser user = UserContext.getCurrentUser();
        if (Objects.isNull(user)) {
            throw HttpException.unauthorized("Unauthorized");
        }

        getCookie(request).ifPresent(sessionDao::deleteByCookie);

        Cookie cookie = new Cookie(LOGIN_COOKE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * @api {post} /api/v1/session 登录
     * @apiName 登录
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/x-www-form-urlencoded
     * @apiParam {String} username 用户名
     * @apiParam {String} password 密码
     * @apiParamExample Request-Example:
     * username: Alice
     * password: MySecretPassword
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 201 Created
     * {
     * "user": {
     * "id": 123,
     * "username": "Alice"
     * }
     * }
     * @apiError 400 Bad Request 若用户的请求包含错误
     * @apiErrorExample Error-Response:
     * HTTP/1.1 400 Bad Request
     * {
     * "message": "Bad Request"
     * }
     */
    @PostMapping("/session")
    @ResponseStatus(HttpStatus.CREATED)
    public SysUserVo login(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response) {
        // TODO 对用户名和密码进行参数校验
        SysUser userInDatabase = sysUserService.getUserByUsername(username);
        if (Objects.isNull(userInDatabase)) {
            throw HttpException.notFound("该用户未注册");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userInDatabase.getEncryptedPassword());
        if (!result.verified) {
            throw HttpException.badRequest("账号或密码错误");
        }

        String cookie = UUID.randomUUID().toString();

        Session session = new Session();
        session.setCookie(cookie);
        session.setUser(userInDatabase);
        sessionDao.save(session);

        response.addCookie(new Cookie(LOGIN_COOKE_NAME, cookie));
        return SysUserVo.toSysUserVo(userInDatabase);
    }


}
