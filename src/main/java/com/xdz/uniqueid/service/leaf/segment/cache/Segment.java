package com.xdz.uniqueid.service.leaf.segment.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 分段信息
 *
 * @author Guoqiang.Wang
 * @version 1.0.0
 * @date 2023/3/14 12:33
 */
public class Segment {
    /**
     * 分段ID
     */
    private final long segmentId;
    /**
     * 开始值
     */
    private final long startValue;
    /**
     * 最大值
     */
    private final long endValue;
    /**
     * 当前值
     */
    private long currentValue;
    /**
     * 是否可用
     */
    private boolean used;

    public Segment(long segmentId, long startValue, long endValue, long currentValue, boolean used) {
        this.segmentId = segmentId;
        this.startValue = startValue;
        this.endValue = endValue;
        this.currentValue = currentValue;
        this.used = used;
    }

    public long getSegmentId() {
        return segmentId;
    }

    public long getStartValue() {
        return startValue;
    }

    public long getEndValue() {
        return endValue;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * map转对象
     *
     * @param map 缓存中的map
     * @return com.xdz.uniqueid.service.leaf.segment.cache.Segment
     * @author wgq
     * @date 2023/3/14 15:15
     **/
    public static Segment fromMap(Map<Object, Object> map) {
        if (map.isEmpty()) {
            return null;
        }
        long segmentId = Long.parseLong(map.get("segmentId").toString());
        long startValue = Long.parseLong(map.get("startValue").toString());
        long endValue = Long.parseLong(map.get("endValue").toString());
        long currentValue = Long.parseLong(map.get("currentValue").toString());
        boolean used = Boolean.parseBoolean(map.get("used").toString());
        return new Segment(segmentId, startValue, endValue, currentValue, used);
    }

    /**
     * 对象转为Map
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author wgq
     * @date 2023/3/14 15:13
     **/
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("segmentId", String.valueOf(segmentId));
        map.put("startValue", String.valueOf(startValue));
        map.put("endValue", String.valueOf(endValue));
        map.put("currentValue", String.valueOf(currentValue));
        map.put("used", String.valueOf(used));
        return map;
    }
}