package com.greek.Course;

import com.greek.Course.global.Result;
import com.greek.Course.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        String usernameAndPassword = "username=Admin3&password=123456";

        // 使用账号密码登录
        HttpResponse<String> response = post("/session", usernameAndPassword);
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
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

}
