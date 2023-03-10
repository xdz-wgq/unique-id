package com.xdz.uniqueid.config;

import com.xdz.uniqueid.service.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * id配置类
 * @author Guoqiang.Wang
 * @date 2023/3/10 15:06
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(IdProperties.class)
public class IdConfig {

    @Resource
    private IdProperties idProperties;

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(idProperties.getMachineRoomId(), idProperties.getMachineId());
    }
}