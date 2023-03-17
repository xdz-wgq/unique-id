package com.xdz.uniqueid.service.snowflake;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 基于雪花算法生成ID
 * @author wgq
 * @date 2023/3/10 14:32
 * @version 1.0.0
 **/
public class SnowflakeService {

    /**
     * 序号占用bit位
     */
    private final static int SEQUENCE_NO_BITS = 12;

    /**
     * 机器ID占用bit位
     */
    private final static int MACHINE_ID_BITS = 5;

    /**
     * 机房ID占用bit位
     */
    private final static int MACHINE_ROOM_ID_BITS = 5;

    /**
     * 机器ID左移位数
     */
    private final static int MACHINE_ID_LEFT_BITS = SEQUENCE_NO_BITS;

    /**
     * 机房ID左移位数
     */
    private final static int MACHINE_ROOM_ID_LEFT_BITS = MACHINE_ID_LEFT_BITS + MACHINE_ID_BITS;

    /**
     * 时间位左移位数
     */
    private final static int TIME_BIT_LEFT_BITS = MACHINE_ROOM_ID_LEFT_BITS + MACHINE_ROOM_ID_BITS;

    /**
     * 最大机房ID
     */
    private final static int MAX_MACHINE_ROOM_ID = 1 << MACHINE_ROOM_ID_BITS - 1;

    /**
     * 最大机器ID
     */
    private final static int MAX_MACHINE_ID = 1 << MACHINE_ID_BITS - 1;

    /**
     * 最大的序列号
     */
    private final static int MAX_SEQUENCE_NO = 1 << SEQUENCE_NO_BITS - 1;

    /**
     * 起始的时间戳：2023-03-10 13:49:38
     * 从起始时间开始，时间戳的位数可以用2^41-1，也就是大概69年
     */
    private final static long START_TIMESTAMP = 1678427378866L;

    /**
     * 上次时间戳ID
     */
    private long lastTimeStamp;

    /**
     * 机房ID
     */
    private final long machineRoomId;

    /**
     * 机器ID
     */
    private final long machineId;

    /**
     * 序列号
     */
    private int sequenceNo = -1;


    /**
     * 根据机房ID和机器ID来保证分布式下唯一
     * @param machineRoomId 机房ID
     * @param machineId 机器ID
     * @author wgq
     * @date 2023/3/10 13:46
     **/
    public SnowflakeService(long machineRoomId, long machineId) {
        if (machineRoomId < 0 || machineRoomId > MAX_MACHINE_ROOM_ID) {
            throw new RuntimeException("机房ID设置超过最大值" + machineRoomId);
        }
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new RuntimeException("机器ID设置超过最大值" + machineId);
        }
        this.machineRoomId = machineRoomId;
        this.machineId = machineId;
        this.lastTimeStamp = START_TIMESTAMP;
    }

    /**
     * 批量获取ID
     * @param size  获取的ID数量
     * @author wgq
     * @date 2023/3/10 15:40
     * @return java.util.List<java.lang.Long>
     **/
    public List<Long> getIds(int size) {
        return IntStream.range(0, size).boxed().map(integer -> getId()).collect(Collectors.toList());
    }

    /**
     * 获取唯一ID
     * @author wgq
     * @date 2023/3/10 13:42
     * @return long
     **/
    public synchronized long getId() {
        long idTimeBit = getIdTimeBit();
        //处理序列号已到最大值的问题
        while (sequenceNo >= MAX_SEQUENCE_NO) {
            idTimeBit = getIdTimeBit();
        }
        sequenceNo++;
        return (idTimeBit << TIME_BIT_LEFT_BITS) | (machineRoomId << MACHINE_ROOM_ID_LEFT_BITS) | (machineId << MACHINE_ID_LEFT_BITS) | sequenceNo;
    }

    /**
     * 获取id中的时间位
     * 处理时钟回拨问题
     * 处理序号归零问题
     * 当前时间戳-起始时间戳
     * @author wgq
     * @date 2023/3/10 14:15
     * @return long
     **/
    private long getIdTimeBit() {
        long currentTimeStamp;
        //时钟回拨
        while ((currentTimeStamp = System.currentTimeMillis()) < lastTimeStamp) {
            //发生了时钟回拨，需要添加报警处理
            System.out.println("时钟回拨了");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                System.out.println("强制中断唤醒线程");
            }
        }
        if (currentTimeStamp > lastTimeStamp) {
            lastTimeStamp = currentTimeStamp;
            sequenceNo = -1;
        }
        return currentTimeStamp - START_TIMESTAMP;
    }
}