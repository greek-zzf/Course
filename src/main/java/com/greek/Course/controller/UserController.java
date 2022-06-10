package com.greek.Course.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.greek.Course.annotation.Admin;
import com.greek.Course.exception.HttpException;
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
 * @date 2022/5/11 14:53
 */

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private SysUserService sysUserService;

    @Autowired
    UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public SysUserVo register(@RequestParam UsernameAndPassword usernameAndPassword) {
        SysUser userInDatabase = sysUserService.getUserByUsername(usernameAndPassword.getUsername());

        if (Objects.nonNull(userInDatabase)) {
            throw HttpException.gone("用户名已经被注册");
        }

        SysUser toSaveUser = new SysUser();
        toSaveUser.setUsername(usernameAndPassword.getUsername());
        toSaveUser.setEncryptedPassword(BCrypt.withDefaults().hashToString(12, usernameAndPassword.getPassword()
                .toCharArray()));
        SysUser savedSysUser = sysUserService.addSysUser(toSaveUser);

        return SysUserVo.toSysUserVo(savedSysUser);
    }

    @PatchMapping("/user/{id}")
    @Admin
    public SysUser updateUser(@PathVariable Integer id, @RequestBody SysUser user) {
        return sysUserService.updateUser(id, user);
    }

    @GetMapping("/user/{id}")
    public SysUser getUser(@PathVariable Integer id) {
        return sysUserService.getUser(id);
    }


}
