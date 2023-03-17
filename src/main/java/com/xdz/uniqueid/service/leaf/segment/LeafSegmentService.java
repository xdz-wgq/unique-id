package com.xdz.uniqueid.service.leaf.segment;

import com.xdz.uniqueid.service.leaf.segment.cache.Segment;
import com.xdz.uniqueid.service.leaf.segment.db.entity.LeafAlloc;
import com.xdz.uniqueid.service.leaf.segment.db.mapper.LeafAllocMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 美团分段算法实现
 *
 * @author wgq
 * @version 1.0.0
 * @date 2023/3/14 12:39
 **/
public class LeafSegmentService {
    /**
     * segment缓存前缀
     */
    private static final String SEGMENT_KEY_PREFIX = "leaf-segments:";
    /**
     * 默认业务标签
     */
    private static final String DEFAULT_BIZ_TAT = "default";
    /**
     * 每个Segment的大小（即最大ID值）
     */
    protected int segmentSize;

    /**
     * 当前Segment的最大ID值
     */
    protected long maxId;

    /**
     * 当前Segment的当前ID值
     */
    protected long currentId;

    /**
     * 分段分配信息持久层操作对象
     */
    private final LeafAllocMapper leafAllocMapper;

    /**
     * 缓存操作对象
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化相关数据
     *
     * @author wgq
     * @date 2023/3/14 12:54
     **/
    public LeafSegmentService(int segmentSize, LeafAllocMapper leafAllocMapper, StringRedisTemplate stringRedisTemplate) {
        this.segmentSize = segmentSize;
        this.leafAllocMapper = leafAllocMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        init();
    }

    /**
     * 批量获取ID
     *
     * @param size 获取的ID数量
     * @return java.util.List<java.lang.Long>
     * @author wgq
     * @date 2023/3/10 15:40
     **/
    public List<Long> getIds(int size) {
        return IntStream.range(0, size).boxed().map(integer -> getId()).collect(Collectors.toList());
    }

    /**
     * 获取唯一ID
     *
     * @return java.lang.Long
     * @author wgq
     * @date 2023/3/14 12:37
     **/
    public long getId() {
        return getId(DEFAULT_BIZ_TAT);
    }

    /**
     * 获取某个业务下的唯一ID
     *
     * @param bizTag 业务标签
     * @return long
     * @author wgq
     * @date 2023/3/14 17:08
     **/
    public synchronized long getId(String bizTag) {
        Segment currentSegment = getCurrentSegment(bizTag);
        long currentValue = currentSegment.getCurrentValue();
        if (currentValue > currentSegment.getEndValue()) {
            // 如果当前Segment已经被使用完，则重新生成新的Segment
            setSegmentUsed(bizTag, currentSegment.getStartValue(), true);
            createNewSegment(bizTag);
        } else {
            currentSegment.setCurrentValue(currentValue + 1);
            persistSegment(bizTag, currentSegment);
        }
        return currentValue;
    }

    /**
     * 初始化相应参数
     *
     * @author wgq
     * @date 2023/3/14 12:41
     **/
    private void init() {
        List<LeafAlloc> leafAllocList = leafAllocMapper.selectList(null);
        if (CollectionUtils.isEmpty(leafAllocList)) {
            leafAllocList.add(addLeafAlloc("default"));
        }
        for (LeafAlloc leafAlloc : leafAllocList) {
            getCurrentSegment(leafAlloc.getBizTag());
        }
    }

    /**
     * 获取业务的分配信息
     *
     * @param bizTag 业务标签
     * @return com.xdz.uniqueid.service.leaf.segment.db.entity.LeafAlloc
     * @author wgq
     * @date 2023/3/14 16:48
     **/
    private LeafAlloc getLeafAlloc(String bizTag) {
        // 根据业务类型查询LeafAlloc记录
        LeafAlloc leafAlloc = leafAllocMapper.selectById(bizTag);
        if (leafAlloc == null) {
            // 如果不存在，则创建一个新的记录
            leafAlloc = new LeafAlloc(bizTag, 0L, segmentSize, "");
            leafAllocMapper.insert(leafAlloc);
        }
        return leafAlloc;
    }

    /**
     * 添加分配信息
     *
     * @param bizTag 业务标签
     * @return com.xdz.uniqueid.service.leaf.segment.db.entity.LeafAlloc
     * @author wgq
     * @date 2023/3/14 16:22
     **/
    private LeafAlloc addLeafAlloc(String bizTag) {
        // 如果没有已分配的Segment信息，则新建一个LeafAlloc记录
        LeafAlloc leafAlloc = new LeafAlloc(bizTag, 0L, segmentSize, "");
        leafAllocMapper.insert(leafAlloc);
        return leafAlloc;
    }

    /**
     * 获取当前业务的分段信息
     *
     * @param bizTag 业务标签
     * @return com.xdz.uniqueid.service.leaf.segment.cache.Segment
     * @author wgq
     * @date 2023/3/14 16:42
     **/
    private Segment getCurrentSegment(String bizTag) {
        // 根据业务类型和当前Segment编号从Redis中获取Segment信息
        String key = SEGMENT_KEY_PREFIX + bizTag;
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        Segment currentSegment = Segment.fromMap(map);
        // 如果没有找到对应的Segment信息，则创建一个新的Segment
        if (currentSegment == null) {
            currentSegment = createNewSegment(bizTag);
        }
        return currentSegment;
    }

    /**
     * 创建新的分段信息
     *
     * @param bizTag 业务标签
     * @return com.xdz.uniqueid.service.leaf.segment.cache.Segment
     * @author wgq
     * @date 2023/3/14 17:05
     **/
    private Segment createNewSegment(String bizTag) {
        // 查询LeafAlloc记录，如果不存在则新建一条记录
        LeafAlloc leafAlloc = getLeafAlloc(bizTag);
        // 计算新Segment的起始值和结束值
        long startValue = leafAlloc.getMaxId() - segmentSize + 1;
        long endValue = leafAlloc.getMaxId();
        // 新建一个Segment并保存到数据库中
        Segment newSegment = new Segment(leafAlloc.getStep(), startValue, endValue, startValue, false);
        // 将新的Segment信息保存到Redis中
        persistSegment(bizTag, newSegment);
        // 更新LeafAlloc记录中的最大ID值
        leafAlloc.setMaxId(endValue);
        leafAllocMapper.updateById(leafAlloc);
        return newSegment;
    }

    /**
     * 持久化分段信息到redis
     *
     * @param bizTag  业务标签
     * @param segment 分段信息
     * @author wgq
     * @date 2023/3/14 16:58
     **/
    private void persistSegment(String bizTag, Segment segment) {
        // 将Segment信息保存到Redis中
        String key = SEGMENT_KEY_PREFIX + bizTag;
        stringRedisTemplate.opsForHash().putAll(key + ":" + segment.getSegmentId(), segment.toMap());
        stringRedisTemplate.expire(key + ":" + segment.getSegmentId(), 7, TimeUnit.DAYS);
    }

    /**
     * 设置分段使用信息
     *
     * @param bizTag     业务标签
     * @param startValue 开始值
     * @param used       使用标记
     * @author wgq
     * @date 2023/3/14 17:12
     **/
    private void setSegmentUsed(String bizTag, Long startValue, boolean used) {
        // 更新Redis中所有起始值大于等于startValue的Segment的状态
        String keyPattern = SEGMENT_KEY_PREFIX + bizTag + ":*";
        Set<String> keyList = stringRedisTemplate.keys(keyPattern);
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        for (String key : keyList) {
            Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
            Segment segment = Segment.fromMap(map);
            if (segment == null) {
                continue;
            }
            if (segment.getStartValue() >= startValue) {
                segment.setUsed(used);
                persistSegment(key, segment);
            }
        }
    }

}
