package com.fj.api.okhttp;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import okhttp3.*;

/**
 * HttpClient,用于发起http请求
 *
 * @Author: FengJie
 * @Date: 2019/5/29 20:12
 */
@UtilityClass
public class HttpClient {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    @SneakyThrows
    public static Response post(HttpUrl.Builder param) {
        val body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), StrUtil.EMPTY);
        return post(param, body);
    }

    @SneakyThrows
    public static Response post(HttpUrl.Builder param, RequestBody body) {
        val req = new Request.Builder().post(body).url(param.build()).build();
        return CLIENT.newCall(req).execute();
    }

    @SneakyThrows
    public static Response call(Request request) {
        return CLIENT.newCall(request).execute();
    }
}
