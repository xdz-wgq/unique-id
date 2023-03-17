package com.xdz.uniqueid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.xdz.uniqueid.service.leaf.segment.db.mapper")
public class UniqueIdApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniqueIdApplication.class, args);
    }

}
