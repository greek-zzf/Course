package com.greek.Course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greek.Course.global.Result;
import com.greek.Course.model.SysUser;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 登录、注册、注销集成测试
 *
 * @author Zhaofeng Zhou
 * @since 2022/6/7
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseApplication.class, webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test_application.yml"})
public class AuthIntegration {

    @Autowired
    Environment environment;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${spring.datasource.url}")
    String databaseUrl;
    @Value("${spring.datasource.username}")
    String databaseUsername;
    @Value("${spring.datasource.password}")
    String databasePassword;


    @BeforeEach
    void initDatabase() {
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void loginTest() throws IOException, InterruptedException {
        String usernameAndPassword = "username=Admin3&password=123456";

        // 使用账号密码登录
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1/" + getPort() + "/api/v1/session"))
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(usernameAndPassword))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertTrue(response.headers().firstValue("Set-Cookie").isPresent());

        // 获取登录状态
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost/" + getPort() + "/api/v1/session"))
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals("Admin3", objectMapper.readValue(response.body(), SysUser.class).getUsername());
    }

    @Test
    public void accountNotFoundAndPasswordErrorTest() throws IOException, InterruptedException {
        // 登录账号不存在
        String usernameAndPassword = "username=zzf1&password=123456";

        // 使用账号密码登录
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost/" + getPort() + "/api/v1/session"))
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(usernameAndPassword))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
        assertEquals("该用户未注册", objectMapper.readValue(response.body(), Result.class).getMsg());

        // 账号存在，密码核对失败

        String usernameAndErrorPassword = "username=Admin3&password=1234567";

        client = HttpClient.newHttpClient();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost/" + getPort() + "/api/v1/session"))
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(usernameAndPassword))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("账号或密码错误", objectMapper.readValue(response.body(), Result.class).getMsg());

    }

    private String getPort() {
        return environment.getProperty("local.server.port");
    }

}
