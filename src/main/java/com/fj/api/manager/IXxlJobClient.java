package com.fj.api.manager;


import com.fj.api.model.ReturnT;
import com.fj.api.model.XxlJobInfo;

import java.util.Map;


/**
 * Xxl-job javaAPI
 *
 * @author 冯杰
 */
public interface IXxlJobClient {
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
    Map<String, Object> pageList(int start, int length, int jobGroup,
                                 String jobDesc, String executorHandler,
                                 String filterTime);

    /**
     * 添加工作，默认停止
     *
     * @param jobInfo job
     * @return return
     */
    ReturnT<String> add(XxlJobInfo jobInfo);

    /**
     * update job, update quartz-cron if started
     * 更新工作，如果启动更新quartz-cron
     *
     * @param jobInfo job
     * @return return
     */
    ReturnT<String> update(XxlJobInfo jobInfo);

    /**
     * remove job, unbind quartz
     * 删除定时任务
     *
     * @param id id
     * @return return
     */
    ReturnT<String> remove(int id);

    /**
     * start job, bind quartz
     * 启动定时任务
     *
     * @param id id
     * @return return
     */
    ReturnT<String> start(int id);

    /**
     * stop job, unbind quartz
     * 停止定时任务
     *
     * @param id id
     * @return return
     */
    ReturnT<String> stop(int id);

    /**
     * 触发执行器
     *
     * @param id            id
     * @param executorParam 参数
     * @return return
     */
    ReturnT<String> triggerJob(int id, String executorParam);
}
