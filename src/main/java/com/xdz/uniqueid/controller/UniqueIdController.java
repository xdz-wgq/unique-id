package com.xdz.uniqueid.controller;

import com.xdz.uniqueid.config.ServiceEnum;
import com.xdz.uniqueid.service.snowflake.SnowflakeService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取唯一ID接口
 * @author Guoqiang.Wang
 * @date 2023/3/10 16:37
 * @version 1.0.0
 */
@RestController
public class UniqueIdController {

    @Resource
    private SnowflakeService snowflakeService;

    @RequestMapping("getId")
    public String getId(String serviceName) {
        return getPrefix(serviceName) + snowflakeService.getId();
    }

    @RequestMapping("getIds")
    public List<String> getIds(String serviceName, int size) {
        return snowflakeService.getIds(size).stream().map(id -> getPrefix(serviceName) + id).collect(Collectors.toList());
    }

    /**
     * 获取业务前缀ID
     * @param serviceName   业务名称
     * @author wgq
     * @date 2023/3/10 17:00
     * @return java.lang.String
     **/
    private String getPrefix(String serviceName) {
        String idPrefix = "";
        if (StringUtils.hasText(serviceName)) {
            idPrefix = ServiceEnum.valueOf(serviceName).getIdPrefix();
        }
        return idPrefix;
    }
}