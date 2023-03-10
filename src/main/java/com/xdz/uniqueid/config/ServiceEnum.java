package com.xdz.uniqueid.config;

/**
 * 业务枚举
 * @author Guoqiang.Wang
 * @date 2023/3/10 16:39
 * @version 1.0.0
 */
public enum ServiceEnum {
    /**
     * 订单前缀
     */
    ORDER("D"),
    /**
     * 商品前缀
     */
    GOODS("S"),
    ;

    private final String idPrefix;

    ServiceEnum(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getIdPrefix() {
        return idPrefix;
    }
}
