package com.greek.Course;

import com.greek.Course.global.Result;
import com.greek.Course.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 登录、注册、注销集成测试
 *
 * @author Zhaofeng Zhou
 * @since 2022/6/7
 */

public class AuthIntegration extends AbstractIntegrationTest {


    @Test
    public void loginTest() throws IOException, InterruptedException {
        String usernameAndPassword = "username=Admin3&password=123456";

        // 使用账号密码登录
        HttpResponse<String> response = post("/api/v1/session", usernameAndPassword);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertTrue(response.headers().firstValue("Set-Cookie").isPresent());

        // 获取登录状态
        response = get("/api/v1/session");
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals("Admin3", objectMapper.readValue(response.body(), SysUser.class).getUsername());
    }

    @Test
    public void accountNotFoundAndPasswordErrorTest() throws IOException, InterruptedException {
        // 登录账号不存在
        String usernameAndPassword = "username=zzf1&password=123456";
        HttpResponse<String> response = post("/api/v1/session", usernameAndPassword);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
        assertEquals("该用户未注册", objectMapper.readValue(response.body(), Result.class).getMsg());

        // 账号存在，密码核对失败
        String usernameAndErrorPassword = "username=Admin3&password=1234567";
        response =  post("/api/v1/session", usernameAndErrorPassword);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("账号或密码错误", objectMapper.readValue(response.body(), Result.class).getMsg());

    }


}
