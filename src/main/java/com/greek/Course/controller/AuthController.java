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

import java.util.Objects;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */

@RestController
@RequestMapping("api/v1")
public class AuthController {

    private SysUserService sysUserService;

    @Autowired
    AuthController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/session")
    public SysUserVo currentUser() {
        SysUser user = UserContext.getUser();
        if (Objects.isNull(user)) {
            throw HttpException.unauthorized("Unauthorized");
        }

        return SysUserVo.toSysUserVo(user);
    }


    @DeleteMapping("/session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        SysUser user = UserContext.getUser();
        if (Objects.isNull(user)) {
            throw HttpException.unauthorized("Unauthorized");
        }
    }

    @PostMapping("/session")
    @ResponseStatus(HttpStatus.CREATED)
    public SysUserVo login(@RequestParam UsernameAndPassword usernameAndPassword) {
        // TODO 对用户名和密码进行参数校验
        SysUser userInDatabase = sysUserService.getUserByUsername(usernameAndPassword.getUsername());
        if (Objects.isNull(userInDatabase)) {
            throw HttpException.notFound("该用户未注册");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(usernameAndPassword.getPassword().toCharArray(), userInDatabase.getEncryptedPassword());
        if (result.verified) {
            throw HttpException.badRequest("账号或密码错误");
        }

        return SysUserVo.toSysUserVo(userInDatabase);
    }


}
