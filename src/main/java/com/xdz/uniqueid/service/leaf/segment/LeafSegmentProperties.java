package com.xdz.uniqueid.service.leaf.segment;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * id配置属性
 * @author Guoqiang.Wang
 * @date 2023/3/10 15:07
 * @version 1.0.0
 */
@ConfigurationProperties("leaf.segment")
public class LeafSegmentProperties {
    /**
     * 分段大小
     */
    private int segmentSize;

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }
}