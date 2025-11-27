package com.wzh.intelligentcodeapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wzh.intelligentcodeapp.mapper")
public class IntelligentCodeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentCodeAppApplication.class, args);
    }

}