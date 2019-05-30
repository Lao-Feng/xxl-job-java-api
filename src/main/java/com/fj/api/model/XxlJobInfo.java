package com.fj.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * xxl-job info
 *
 * @author xuxueli  2016-1-12 18:25:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XxlJobInfo {

    private int id;                // 主键ID	    (JobKey.name)

    private int jobGroup;        // 执行器主键ID	(JobKey.group)
    private String jobCron;        // 任务执行CRON表达式 【base on quartz】
    private String jobDesc;

    //@JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    //@JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String author;        // 负责人
    private String alarmEmail;    // 报警邮件

    private String executorRouteStrategy;    // 执行器路由策略
    private String executorHandler;            // 执行器，任务Handler名称
    private String executorParam;            // 执行器，任务参数
    private String executorBlockStrategy;    // 阻塞处理策略
    private int executorTimeout;            // 任务执行超时时间，单位秒
    private int executorFailRetryCount;        // 失败重试次数

    private String glueType;        // GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
    private String glueSource;        // GLUE源代码
    private String glueRemark;        // GLUE备注
    //@JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date glueUpdatetime;    // GLUE更新时间

    private String childJobId;        // 子任务ID，多个逗号分隔

    // copy from quartz
    private String jobStatus;        // 任务状态 【base on quartz】
}
