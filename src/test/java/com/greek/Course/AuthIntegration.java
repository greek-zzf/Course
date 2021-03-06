package com.greek.Course;

import com.greek.Course.global.Result;
import com.greek.Course.model.Session;
import com.greek.Course.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 登录、注册、注销集成测试
 *
 * @author Zhaofeng Zhou
 * @since 2022/6/7
 */

public class AuthIntegration extends AbstractIntegrationTest {


    /**
     * 用户注册、登录、注销流程测试，详细流程如下：
     * <p>
     * 1. 注册用户
     * 2. 用户登录
     * 3. 检查用户登录状态
     * 4. 用户注销
     * 5. 检查用户登录状态
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void registerLoginLogoutTest() throws IOException, InterruptedException {
        String usernameAndPassword = "username=Admin4&password=123456";
        // 注册用户
        HttpResponse<String> response = post("/user", usernameAndPassword);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals("Admin4", objectMapper.readValue(response.body(), SysUser.class).getUsername());

        // 使用注册的账号密码登录
        response = post("/session", usernameAndPassword);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        String cookie = response.headers().firstValue("Set-Cookie").get();
        assertNotNull(cookie);

        // 获取登录状态
        response = get("/session", cookie);
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals("Admin4", objectMapper.readValue(response.body(), Session.class).getUser().getUsername());

        // 注销用户
        response = delete("/session", cookie);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());

        // 再次获取登录状态
        response = get("/session");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());
    }

    @Test
    public void accountNotFoundAndPasswordErrorTest() throws IOException, InterruptedException {
        // 登录账号不存在
        String usernameAndPassword = "username=zzf1&password=123456";
        HttpResponse<String> response = post("/session", usernameAndPassword);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
        assertEquals("该用户未注册", objectMapper.readValue(response.body(), Result.class).getMsg());

        // 账号存在，密码核对失败
        String usernameAndErrorPassword = "username=Admin3&password=1234567";
        response = post("/session", usernameAndErrorPassword);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("账号或密码错误", objectMapper.readValue(response.body(), Result.class).getMsg());

    }


    @Test
    public void getErrorIfUsernameAlreadyRegistered() throws IOException, InterruptedException {
        // 注册用户
        String usernameAndPassword = "username=zhangsan&password=123456";
        HttpResponse<String> response = post("/user", usernameAndPassword);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = post("/user", usernameAndPassword);
        assertEquals(HttpStatus.GONE.value(), response.statusCode());
    }

}
