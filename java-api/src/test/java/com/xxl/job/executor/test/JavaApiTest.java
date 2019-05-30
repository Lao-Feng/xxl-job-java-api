package com.xxl.job.executor.test;

import cn.hutool.core.util.StrUtil;
import com.xxljob.javaapi.JobApplication;
import com.xxljob.javaapi.config.XxlJobProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;


/**
 * TODO
 *
 * @Author: FengJie
 * @Date: 2019/5/29 15:08
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobApplication.class)
public class JavaApiTest {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE);
    private final OkHttpClient client = new OkHttpClient();
    @Autowired
    private XxlJobProperties properties;
    @Test
    public void login() {
        String username = properties.getLogin().getUsername();
        String password = properties.getLogin().getPassword();
        String loginUrl = "http://localhost:8080/xxl-job-admin/login";
        System.out.println(login(loginUrl, username, password));
    }

    @SneakyThrows
    private String login(String url, String username, String password) {
        var urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("userName", username);
        urlBuilder.addQueryParameter("password", password);
        urlBuilder.addQueryParameter("ifRemember", "on");
        val body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), "");

        val req = new Request.Builder()
                .post(body)
                .url(urlBuilder.build())
                .build();
        Response execute = client.newCall(req).execute();


        String cookie = Optional.of(execute)
                .filter(Response::isSuccessful)
                .map(r -> r.header("Set-Cookie"))
                .map(c -> StrUtil.sub(c, c.indexOf("=") + 1, c.indexOf(";")))
                .orElse("无" + StrUtil.EMPTY);

        log.trace("http request: url={}, content={}, result={}", url, "", cookie);
        return cookie;
    }

    @Test
    public void pageList() throws IOException {
        String pageListUrl = "http://localhost:8080/xxl-job-admin/jobinfo/pageList";
        System.out.println(post(pageListUrl, ""));
    }

    @SneakyThrows
    private String post(String url, String json) {
        String cookie = "XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437";

        var urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("jobGroup", "1");

        val body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);

        val req = new Request.Builder()
                .url(urlBuilder.build())
                .post(body)
                .addHeader("Cookie", cookie)
                .build();

        Response execute = client.newCall(req).execute();
        System.out.println("是否被重定向:" +execute.isRedirect());
        val result = Optional.of(execute)
                .filter(Response::isSuccessful)
                .map(Response::body)
                .map(this::string)
                .orElse(StrUtil.EMPTY);
        log.trace("http request: url={}, content={}, result={}", url, json, result);
        return result;
    }

    @SneakyThrows
    private String string(ResponseBody body) {
        return body.string();
    }
}
