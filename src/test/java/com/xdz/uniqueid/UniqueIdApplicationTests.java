package com.xdz.uniqueid;

import com.xdz.uniqueid.service.leaf.segment.LeafSegmentService;
import com.xdz.uniqueid.service.leaf.snowflake.LeafSnowflakeService;
import com.xdz.uniqueid.service.snowflake.SnowflakeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UniqueIdApplicationTests {

    @Resource
    private SnowflakeService snowflakeService;

    @Resource
    private LeafSnowflakeService leafSnowflakeService;

    @Resource
    private LeafSegmentService leafSegmentService;

    @Test
    void contextLoads() {
        long startTime = System.currentTimeMillis();
        long id1 = snowflakeService.getId();
        long id2 = leafSnowflakeService.getId();
        long id3 = leafSegmentService.getId();
        System.out.println(id1);
        System.out.println(id2);
        System.out.println(id3);
        System.out.println("生成ID总花费时间为:" + (System.currentTimeMillis() - startTime));
    }
}
