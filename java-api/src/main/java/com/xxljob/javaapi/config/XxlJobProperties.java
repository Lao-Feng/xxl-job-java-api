package com.xxljob.javaapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author 冯杰
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    private Admin admin;
    private Executor executor;
    private Login login;
    private String accessToken;

    @Data
    public static class Admin {
        private String addresses;
    }

    @Data
    public static class Executor {
        private String appname;
        private String ip;
        private Integer port;
        private String logpath;
        private Integer logretentiondays;

    }

    @Data
    public static class Login {
        private String username;
        private String password;
    }
}
