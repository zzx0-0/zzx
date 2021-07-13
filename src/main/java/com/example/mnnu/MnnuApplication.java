package com.example.mnnu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
//@EnableDiscoveryClient
@EnableRetry
@MapperScan(basePackages = "com.example.mnnu.dao")
public class MnnuApplication {

    public static void main(String[] args) {
        SpringApplication.run(MnnuApplication.class, args);
    }

    // AtomicInteger
}

/**
 *
 * 正式发布到服务器，需改：
 *    UserServiceImpl   182行
 *
 *    teacher.html的websocket 地址
 *
 */
