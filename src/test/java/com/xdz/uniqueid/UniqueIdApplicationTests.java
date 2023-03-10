package com.xdz.uniqueid;

import com.xdz.uniqueid.service.IdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UniqueIdApplicationTests {

    @Resource
    private IdWorker idWorker;

    @Test
    void contextLoads() {
        long startTime = System.currentTimeMillis();
        List<Long> ids = idWorker.getIds(100);
        System.out.println(ids.get(0));
        System.out.println("生成ID总花费时间为:" + (System.currentTimeMillis() - startTime));
    }
}
