package com.gmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.gmall.manage")
@SpringBootApplication
public class GmallManageSerivceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallManageSerivceApplication.class, args);
    }

}
