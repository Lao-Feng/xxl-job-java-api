package com.fj.api.test;

import com.alibaba.fastjson.JSON;
import com.fj.api.ApiApplication;
import com.fj.api.manager.XxlJobClientImpl;
import com.fj.api.model.XxlJobInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

/**
 * 见证奇迹的时候到了
 *
 * @Author: FengJie
 * @Date: 2019/5/30 11:25
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class ApiTest {

    @Autowired
    XxlJobClientImpl client;

    @Test
    public void pageList() {
        val result = client.pageList(0, 100, 1,
                "", "", "");
        log.info(Objects.toString(result));
    }

    @Test
    public void add() {
        //在这里不用传递时间、id等
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

        val jobInfo = JSON.parseObject(job, XxlJobInfo.class);
        val result = client.add(jobInfo);
        log.info(Objects.toString(result));
    }

    @Test
    public void start() {
        add();
        client.start(8);
    }

    @Test
    public void stop() {
        client.stop(8);
    }

    @Test
    public void triggerJob() {
        client.triggerJob(8, "手动促发参数");
    }

}
