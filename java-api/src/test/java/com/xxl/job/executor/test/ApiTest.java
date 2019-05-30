package com.xxl.job.executor.test;

import com.alibaba.fastjson.JSON;
import com.xxljob.javaapi.JobApplication;
import com.xxljob.javaapi.manager.XxlJobClientImpl;
import com.xxljob.javaapi.model.ReturnT;
import com.xxljob.javaapi.model.XxlJobInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * TODO
 *
 * @Author: FengJie
 * @Date: 2019/5/30 11:25
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobApplication.class)
public class ApiTest {
    @Autowired
    XxlJobClientImpl client;

    @Test
    public void pageList() {
        Map<String, Object> x = client.pageList(0, 100, 1, "", "", "");
        System.out.println(x);
    }

    @Test
    public void add() {
        //在这里不用传递时间
        String job = "{\"alarmEmail\":\"\"," +
                //"\"glueUpdatetime\":1541254891000," +
                "\"executorParam\":\"\",\"jobStatus\":\"NONE\"," +
                //"\"addTime\":1541254891000," +
                "\"executorBlockStrategy\":\"SERIAL_EXECUTION\",\"author\":\"XXL\"," +
                "\"executorRouteStrategy\":\"FIRST\",\"childJobId\":\"\",\"jobCron\":\"* * * * * ? *\"," +
                //"\"updateTime\":1541254891000," +
                "\"jobGroup\":3,\"glueRemark\":\"GLUE代码初始化\"," +
                "\"jobDesc\":\"测试任务2\",\"glueSource\":\"\",\"glueType\":\"BEAN\"," +
                "\"executorHandler\":\"demoJobHandler\",\"executorFailRetryCount\":0,\"id\":10," +
                "\"executorTimeout\":0}";
        XxlJobInfo jobInfo = JSON.parseObject(job, XxlJobInfo.class);
        ReturnT<String> result = client.add(jobInfo);
        System.out.println(result);
    }

    @Test
    public void start() {
        add();
        client.start(8);
    }

    @Test
    public void stop() {
        add();
        client.stop(8);
    }

    @Test
    public void triggerJob() {
        client.triggerJob(8, "手动促发参数");
    }

}
