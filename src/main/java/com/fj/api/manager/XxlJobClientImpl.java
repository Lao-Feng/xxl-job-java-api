package com.fj.api.manager;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fj.api.config.XxlJobProperties;
import com.fj.api.model.ReturnT;
import com.fj.api.model.XxlJobInfo;
import com.fj.api.okhttp.HttpClient;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * XXL-JOB 对应的javaAPI,提供基本操作定时任务的功能
 * 如果扩展
 *
 * @Author: FengJie
 * @Date: 2019/5/29 21:10
 */
@Component
public class XxlJobClientImpl extends AbstractXxlJobClientTemplate {

    @Autowired
    public XxlJobClientImpl(XxlJobProperties properties) {
        super(properties);
    }

    /**
     * 分页查询任务列表
     *
     * @param addr            addr
     * @param cookie          cookie
     * @param start           起始数
     * @param length          长度
     * @param jobGroup        job组号
     * @param jobDesc         描述
     * @param executorHandler handler
     * @param filterTime      过滤时间
     * @return 列表
     */
    @Override
    Map<String, Object> pageList(String addr, String cookie,
                                 int start, int length,
                                 int jobGroup, String jobDesc,
                                 String executorHandler, String filterTime) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + PAGE_LIST).newBuilder()
                .addQueryParameter("start", Objects.toString(start))
                .addQueryParameter("length", Objects.toString(length))
                .addQueryParameter("jobGroup", Objects.toString(jobGroup))
                .addQueryParameter("jobDesc", jobDesc)
                .addQueryParameter("executorHandler", executorHandler)
                .addQueryParameter("filterTime", filterTime)
                .build();
        val result = call(param, cookie);
        return JSON.parseObject(result);
    }

    /**
     * 添加工作，默认停止
     *
     * @param addr    addr
     * @param cookie  cookie
     * @param jobInfo job
     * @return return
     */
    @Override
    ReturnT<String> add(String addr, String cookie, XxlJobInfo jobInfo) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + ADD_PATH).newBuilder();
        addParam(param, jobInfo);
        val result = call(param.build(), cookie);
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    /**
     * update job, update quartz-cron if started
     * 更新工作，如果启动更新quartz-cron
     *
     * @param addr    addr
     * @param cookie  cookie
     * @param jobInfo job
     * @return return
     */
    @Override
    ReturnT<String> update(String addr, String cookie, XxlJobInfo jobInfo) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + UPDATE_PATH).newBuilder();
        addParam(param, jobInfo);
        val result = call(param.build(), cookie);
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    /**
     * remove job, unbind quartz
     * 删除定时任务
     *
     * @param addr   addr
     * @param cookie cookie
     * @param id     id
     * @return return
     */
    @Override
    ReturnT<String> remove(String addr, String cookie, int id) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + REMOVE_PATH).newBuilder()
                .addQueryParameter("id", Objects.toString(id))
                .build();
        val result = call(param, cookie);
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    /**
     * start job, bind quartz
     * 启动定时任务
     *
     * @param addr   addr
     * @param cookie cookie
     * @param id     id
     * @return return
     */
    @Override
    ReturnT<String> start(String addr, String cookie, int id) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + START_PATH).newBuilder()
                .addQueryParameter("id", Objects.toString(id))
                .build();
        val result = call(param, cookie);
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    /**
     * stop job, unbind quartz
     * 停止定时任务
     *
     * @param addr   addr
     * @param cookie cookie
     * @param id     id
     * @return return
     */
    @Override
    ReturnT<String> stop(String addr, String cookie, int id) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + STOP_PATH).newBuilder()
                .addQueryParameter("id", Objects.toString(id))
                .build();
        val result = call(param, cookie);
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    /**
     * 触发执行器
     *
     * @param addr          addr
     * @param cookie        cookie
     * @param id            id
     * @param executorParam 参数
     * @return return
     */
    @Override
    ReturnT<String> triggerJob(String addr, String cookie, int id, String executorParam) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + TRIGGER_PATH).newBuilder()
                .addQueryParameter("id", Objects.toString(id))
                .addQueryParameter("executorParam", executorParam)
                .build();
        val result = call(param, cookie);
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    /**
     * 将对象的属性添加到请求参数中
     */
    private void addParam(HttpUrl.Builder param, XxlJobInfo jobInfo) {
        //借助JSON工具,将所有的属性和对应的值,以参数的形式传递.
        Optional.of(jobInfo)
                .map(JSON::toJSON)
                .map(JSON::toJSONString)
                .map(JSON::parseObject)
                .get()
                .forEach((k, v) -> param.addQueryParameter(k, Objects.toString(v)));
    }

    /**
     * 发起http调用
     */
    private String call(HttpUrl param, String cookie) {
        val body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), StrUtil.EMPTY);
        val req = new Request
                .Builder()
                .post(body)
                .url(param)
                .addHeader("Cookie", LOGIN_IDENTITY_KEY + cookie)
                .build();
        return Optional.of(req)
                .map(HttpClient::call)
                .filter(Response::isSuccessful)
                .map(Response::body)
                .map(this::string)
                .orElse("{}");
    }

    @SneakyThrows
    private String string(ResponseBody body) {
        return body.string();
    }
}
