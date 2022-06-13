package com.greek.Course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/7
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseApplication.class, webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test_application.yml"})
public class AbstractIntegrationTest {


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

    HttpClient client = HttpClient.newHttpClient();

    private static final String DOMAIN = "http://localhost:";

    @BeforeEach
    void initDatabase() {
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    String getPort() {
        return environment.getProperty("local.server.port");
    }

    public HttpResponse<String> delete(String path) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(DOMAIN + getPort() + "/api/v1" + path))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> delete(String path, String cookie) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(DOMAIN + getPort() + "/api/v1" + path))
                .header("Cookie", cookie)
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public HttpResponse<String> get(String path) throws IOException, InterruptedException {
        return get(path, Map.of());
    }

    public HttpResponse<String> get(String path, String cookie) throws IOException, InterruptedException {
        return get(path, Map.of("Cookie", cookie));
    }

    public HttpResponse<String> get(String path, Map<String, String> headers) throws IOException, InterruptedException {
        var builder = HttpRequest.newBuilder(URI.create(DOMAIN + getPort() + "/api/v1" + path));
        headers.forEach(builder::header);
        builder.header("Accept", APPLICATION_JSON_VALUE);
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String path, String body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(DOMAIN + getPort() + "/api/v1" + path))
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
