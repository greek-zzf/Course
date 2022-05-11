package com.greek.Course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhaofeng Zhou
 * @date 2022/5/11 14:53
 */

@Controller
public class LoginController {

    private static Map<String, String> usernameToPassword = new HashMap<>();

    static {
        usernameToPassword.put("admin", "123");
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam UsernameAndPassword usernameAndPassword) {

        String password = usernameToPassword.getOrDefault(usernameAndPassword.getUsername(), "");

        if (password.equals(usernameAndPassword.getPassword())) {
            return "LOGIN SUCCESSED";
        }

        return "FAIL";
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
