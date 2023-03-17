package com.xdz.uniqueid.service.leaf.snowflake;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * id配置属性
 * @author Guoqiang.Wang
 * @date 2023/3/10 15:07
 * @version 1.0.0
 */
@ConfigurationProperties("leaf.snowflake")
public class LeafSnowflakeProperties {
    /**
     * 机房ID
     */
    private String zkConnectStr;

    /**
     * 机器ID
     */
    private int zkSessionTimeout;

    public String getZkConnectStr() {
        return zkConnectStr;
    }

    public void setZkConnectStr(String zkConnectStr) {
        this.zkConnectStr = zkConnectStr;
    }

    public int getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public void setZkSessionTimeout(int zkSessionTimeout) {
        this.zkSessionTimeout = zkSessionTimeout;
    }
}