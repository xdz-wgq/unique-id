package com.xdz.uniqueid.config;

import com.xdz.uniqueid.service.leaf.segment.LeafSegmentProperties;
import com.xdz.uniqueid.service.leaf.segment.LeafSegmentService;
import com.xdz.uniqueid.service.leaf.segment.db.mapper.LeafAllocMapper;
import com.xdz.uniqueid.service.leaf.snowflake.LeafSnowflakeProperties;
import com.xdz.uniqueid.service.leaf.snowflake.LeafSnowflakeService;
import com.xdz.uniqueid.service.snowflake.SnowflakeProperties;
import com.xdz.uniqueid.service.snowflake.SnowflakeService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * id配置类
 *
 * @author Guoqiang.Wang
 * @version 1.0.0
 * @date 2023/3/10 15:06
 */
@Configuration
@EnableConfigurationProperties({SnowflakeProperties.class, LeafSnowflakeProperties.class, LeafSegmentProperties.class})
public class UniqueIdConfig {

    @Resource
    private SnowflakeProperties snowflakeProperties;


    @Resource
    private LeafSnowflakeProperties leafSnowflakeProperties;

    @Resource
    private LeafSegmentProperties leafSegmentProperties;

    @Bean
    public SnowflakeService snowflakeService() {
        return new SnowflakeService(snowflakeProperties.getMachineRoomId(), snowflakeProperties.getMachineId());
    }

    @Bean
    public LeafSnowflakeService leafSnowflakeService() {
        return new LeafSnowflakeService(leafSnowflakeProperties.getZkConnectStr(), leafSnowflakeProperties.getZkSessionTimeout());
    }

    @Bean
    public LeafSegmentService leafSegmentService(LeafAllocMapper leafAllocMapper, StringRedisTemplate stringRedisTemplate) {
        return new LeafSegmentService(leafSegmentProperties.getSegmentSize(), leafAllocMapper, stringRedisTemplate);
    }
}