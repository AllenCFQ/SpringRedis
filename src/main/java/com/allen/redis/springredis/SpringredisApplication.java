package com.allen.redis.springredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringredisApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringredisApplication.class, args);
        TestRedis bean = ctx.getBean(TestRedis.class);
        bean.testRedis();
    }
}
