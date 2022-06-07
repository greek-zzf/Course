package com.greek.Course.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.greek.Course.exception.HttpException;
import com.greek.Course.global.UserContext;
import com.greek.Course.model.SysUser;
import com.greek.Course.model.vo.SysUserVo;
import com.greek.Course.model.vo.UsernameAndPassword;
import com.greek.Course.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

import static com.greek.Course.interceptor.LoginInterceptor.LOGIN_COOKE_NAME;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private SysUserService sysUserService;

    @Autowired
    AuthController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }


    /**
     * @api {get} /api/v1/session 检查登录状态
     * @apiName 检查登录状态
     * @apiGroup 登录与鉴权
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParamExample Request-Example:
     *            GET /api/v1/auth
     *
     * @apiSuccess {User} user 用户信息
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "user": {
     *           "id": 123,
     *           "username": "Alice"
     *       }
     *     }
     * @apiError 401 Unauthorized 若用户未登录
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     * @return 已登录的用户
     */
    @GetMapping("/session")
    public SysUserVo currentUser() {
        SysUser user = UserContext.getCurrentUser();
        if (Objects.isNull(user)) {
            throw HttpException.unauthorized("Unauthorized");
        }

        return SysUserVo.toSysUserVo(user);
    }


    /**
     * @api {delete} /api/v1/session 登出
     * @apiName 登出
     * @apiGroup 登录与鉴权
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParamExample Request-Example:
     *            DELETE /api/v1/session
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 204 No Content
     * @apiError 401 Unauthorized 若用户未登录
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    @DeleteMapping("/session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        SysUser user = UserContext.getCurrentUser();
        if (Objects.isNull(user)) {
            throw HttpException.unauthorized("Unauthorized");
        }
    }


    /**
     * @api {post} /api/v1/session 登录
     * @apiName 登录
     * @apiGroup 登录与鉴权
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/x-www-form-urlencoded
     *
     * @apiParam {String} username 用户名
     * @apiParam {String} password 密码
     * @apiParamExample Request-Example:
     *          username: Alice
     *          password: MySecretPassword
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *       "user": {
     *           "id": 123,
     *           "username": "Alice"
     *       }
     *     }
     *
     * @apiError 400 Bad Request 若用户的请求包含错误
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 Bad Request
     *     {
     *       "message": "Bad Request"
     *     }
     */
    @PostMapping("/session")
    @ResponseStatus(HttpStatus.CREATED)
    public SysUserVo login(@RequestParam UsernameAndPassword usernameAndPassword, HttpServletResponse response) {
        // TODO 对用户名和密码进行参数校验
        SysUser userInDatabase = sysUserService.getUserByUsername(usernameAndPassword.getUsername());
        if (Objects.isNull(userInDatabase)) {
            throw HttpException.notFound("该用户未注册");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(usernameAndPassword.getPassword().toCharArray(), userInDatabase.getEncryptedPassword());
        if (result.verified) {
            throw HttpException.badRequest("账号或密码错误");
        }

        response.addCookie(new Cookie(LOGIN_COOKE_NAME, UUID.randomUUID().toString()));
        return SysUserVo.toSysUserVo(userInDatabase);
    }


}
