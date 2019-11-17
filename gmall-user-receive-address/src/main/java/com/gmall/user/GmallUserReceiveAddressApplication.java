package com.gmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.gmall.user.mapper")
@SpringBootApplication
public class GmallUserReceiveAddressApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUserReceiveAddressApplication.class, args);
    }

}
