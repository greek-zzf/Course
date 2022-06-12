package com.greek.Course.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.greek.Course.annotation.Admin;
import com.greek.Course.exception.HttpException;
import com.greek.Course.model.PageResponse;
import com.greek.Course.model.SysUser;
import com.greek.Course.model.vo.SysUserVo;
import com.greek.Course.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
    public SysUserVo register(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        SysUser userInDatabase = sysUserService.getUserByUsername(username);

        if (Objects.nonNull(userInDatabase)) {
            throw HttpException.gone("用户名已经被注册");
        }

        SysUser toSaveUser = new SysUser();
        toSaveUser.setUsername(username);
        toSaveUser.setEncryptedPassword(BCrypt.withDefaults().hashToString(12, password.toCharArray()));
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


    @GetMapping("/user")
    public PageResponse getAllUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType) {

        if (orderType != null && orderBy == null) {
            throw HttpException.badRequest("缺少orderBy!");
        }

        Page<SysUser> response = sysUserService.getAllUsers(search, pageSize, pageNum, orderBy, orderType == null ? null : Sort.Direction.fromString(orderType));
        return PageResponse.of(response, pageNum, pageSize);
    }


}
