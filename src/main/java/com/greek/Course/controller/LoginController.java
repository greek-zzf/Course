package com.greek.Course.controller;

import cn.hutool.core.util.StrUtil;
import com.greek.Course.model.SysUser;
import com.greek.Course.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zhaofeng Zhou
 * @date 2022/5/11 14:53
 */

@Controller
public class LoginController {

    private SysUserService sysUserService;

    LoginController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    private static Map<String, String> usernameToPassword = new HashMap<>();

    static {
        usernameToPassword.put("admin", "123");
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam UsernameAndPassword usernameAndPassword) {
        // TODO 对用户名和密码进行参数校验

        Optional<SysUser> user = sysUserService.getUserByUsername(usernameAndPassword.getUsername());

        user.filter(e -> checkPassword(usernameAndPassword.getPassword(), e.getEncryptedPassword()));
        return user.isPresent() ? "SUCCEED" : "FAIL";
    }

    // TODO 对密码进行加密处理
    private boolean checkPassword(String originPassword, String encryptedPassword) {
        return StrUtil.equals(originPassword, encryptedPassword);
    }


    public static class UsernameAndPassword {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}
