package com.xdz.uniqueid.service.snowflake;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * id配置属性
 * @author Guoqiang.Wang
 * @date 2023/3/10 15:07
 * @version 1.0.0
 */
@ConfigurationProperties("snowflake")
public class SnowflakeProperties {
    /**
     * 机房ID
     */
    private int machineRoomId;

    /**
     * 机器ID
     */
    private int machineId;

    public int getMachineRoomId() {
        return machineRoomId;
    }

    public void setMachineRoomId(int machineRoomId) {
        this.machineRoomId = machineRoomId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}