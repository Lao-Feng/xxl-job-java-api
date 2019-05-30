package com.fj.api.manager;

import cn.hutool.core.util.StrUtil;
import com.fj.api.config.XxlJobProperties;
import com.fj.api.model.ReturnT;
import com.fj.api.model.XxlJobInfo;
import com.fj.api.okhttp.HttpClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.HttpUrl;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Xxl-job 客户端模板
 *
 * @Author: FengJie
 * @Date: 2019/5/29 20:14
 */
@Slf4j
public abstract class AbstractXxlJobClientTemplate implements IXxlJobClient {
    /**
     * XXL_JOB 对应的各种操作路径path
     */
    private final String LOGIN_PATH = "/login";
    final String ADD_PATH = "/jobinfo/add";
    final String PAGE_LIST = "/jobinfo/pageList";
    final String UPDATE_PATH = "/jobinfo/update";
    final String REMOVE_PATH = "/jobinfo/remove";
    final String STOP_PATH = "/jobinfo/stop";
    final String START_PATH = "/jobinfo/start";
    final String TRIGGER_PATH = "/jobinfo/trigger";
    final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY=";

    private String[] addrs;
    private String cookie;
    private XxlJobProperties properties;

    public AbstractXxlJobClientTemplate(XxlJobProperties properties) {
        this.properties = properties;
        val addresses = Optional.of(properties.getAdmin().getAddresses())
                .orElseThrow(() -> new RuntimeException("没有设置xxl-job调度中心ip"));
        this.addrs = StrUtil.split(addresses, ",");
    }

    /**
     * 登录到xxl
     * 主要是为了获取cookie
     */
    @SneakyThrows
    private boolean login(String addr) {
        //noinspection ConstantConditions
        val param = HttpUrl.parse(addr + LOGIN_PATH).newBuilder()
                .addQueryParameter("userName", properties.getLogin().getUsername())
                .addQueryParameter("password", properties.getLogin().getPassword())
                .addQueryParameter("ifRemember", "on");
        val post = HttpClient.post(param);
        val cookie = post.header("Set-Cookie");
        if (post.isSuccessful() && Objects.nonNull(cookie)) {
            this.cookie = StrUtil.sub(cookie, cookie.indexOf("=") + 1, cookie.indexOf(";"));
            return true;
        }
        log.error("登录到xxl-job-admin失败,请检查账户:{} 密码:{}",
                properties.getLogin().getUsername(),
                properties.getLogin().getPassword());
        return false;
    }

    /**
     * 分页查询任务列表
     *
     * @param start           起始数
     * @param length          长度
     * @param jobGroup        job组号
     * @param jobDesc         描述
     * @param executorHandler handler
     * @param filterTime      过滤时间
     * @return 列表
     */
    @Override
    public Map<String, Object> pageList(int start, int length, int jobGroup,
                                        String jobDesc, String executorHandler,
                                        String filterTime) {
        for (String addr : addrs) {
            if (login(addr)) {
                return pageList(addr, cookie, start, length, jobGroup, jobDesc, executorHandler, filterTime);
            }
        }
        return null;
    }

    /**
     * 添加工作，默认停止
     *
     * @param jobInfo job
     * @return return
     */
    @Override
    public ReturnT<String> add(XxlJobInfo jobInfo) {
        for (String addr : addrs) {
            if (login(addr)) {
                return add(addr, cookie, jobInfo);
            }
        }
        return null;
    }

    /**
     * update job, update quartz-cron if started
     * 更新工作，如果启动更新quartz-cron
     *
     * @param jobInfo job
     * @return return
     */
    @Override
    public ReturnT<String> update(XxlJobInfo jobInfo) {
        for (String addr : addrs) {
            if (login(addr)) {
                return update(addr, cookie, jobInfo);
            }
        }
        return null;
    }

    /**
     * remove job, unbind quartz
     * 删除定时任务
     *
     * @param id id
     * @return return
     */
    @Override
    public ReturnT<String> remove(int id) {
        for (String addr : addrs) {
            if (login(addr)) {
                return remove(addr, cookie, id);
            }
        }
        return null;
    }

    /**
     * start job, bind quartz
     * 启动定时任务
     *
     * @param id id
     * @return return
     */
    @Override
    public ReturnT<String> start(int id) {
        for (String addr : addrs) {
            if (login(addr)) {
                return start(addr, cookie, id);
            }
        }
        return null;

    }

    /**
     * stop job, unbind quartz
     * 停止定时任务
     *
     * @param id id
     * @return return
     */
    @Override
    public ReturnT<String> stop(int id) {
        for (String addr : addrs) {
            if (login(addr)) {
                return stop(addr, cookie, id);
            }
        }
        return null;
    }

    /**
     * 粗发执行器
     *
     * @param id            id
     * @param executorParam 参数
     * @return return
     */
    @Override
    public ReturnT<String> triggerJob(int id, String executorParam) {
        for (String addr : addrs) {
            if (login(addr)) {
                return triggerJob(addr, cookie, id, executorParam);
            }
        }
        return null;
    }

    //abstract function

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
    abstract Map<String, Object> pageList(String addr, String cookie, int start, int length, int jobGroup, String jobDesc, String
            executorHandler, String filterTime);

    /**
     * 添加工作，默认停止
     *
     * @param jobInfo job
     * @param cookie  cookie
     * @param addr    addr
     * @return return
     */
    abstract ReturnT<String> add(String addr, String cookie, XxlJobInfo jobInfo);

    /**
     * update job, update quartz-cron if started
     * 更新工作，如果启动更新quartz-cron
     *
     * @param jobInfo job
     * @param cookie  cookie
     * @param addr    addr
     * @return return
     */
    abstract ReturnT<String> update(String addr, String cookie, XxlJobInfo jobInfo);

    /**
     * remove job, unbind quartz
     * 删除定时任务
     *
     * @param id     id
     * @param cookie cookie
     * @param addr   addr
     * @return return
     */
    abstract ReturnT<String> remove(String addr, String cookie, int id);

    /**
     * start job, bind quartz
     * 启动定时任务
     *
     * @param id     id
     * @param cookie cookie
     * @param addr   addr
     * @return return
     */
    abstract ReturnT<String> start(String addr, String cookie, int id);

    /**
     * stop job, unbind quartz
     * 停止定时任务
     *
     * @param id     id
     * @param addr   addr
     * @param cookie cookie
     * @return return
     */
    abstract ReturnT<String> stop(String addr, String cookie, int id);

    /**
     * 粗发执行器
     *
     * @param addr          addr
     * @param cookie        cookie
     * @param id            id
     * @param executorParam 参数
     * @return return
     */
    abstract ReturnT<String> triggerJob(String addr, String cookie, int id, String executorParam);
}
