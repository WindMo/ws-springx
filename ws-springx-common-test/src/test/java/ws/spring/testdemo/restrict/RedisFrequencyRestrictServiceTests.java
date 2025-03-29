package ws.spring.testdemo.restrict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ws.spring.restrict.FrequencyRestrictService;
import ws.spring.restrict.support.RedisFrequencyRestrictService;
import ws.spring.testdemo.SpringxApp;
import ws.spring.testdemo.anno.EnableEmbeddedRedis;

/**
 * @author WindShadow
 * @version 2024-10-17.
 */
@ActiveProfiles("redis")
@EnableEmbeddedRedis
@SpringBootTest(classes = SpringxApp.class, properties = {"app.frequency-restrictor=redis"})
public class RedisFrequencyRestrictServiceTests extends FrequencyRestrictServiceTests {

    @Autowired
    private RedisFrequencyRestrictService restrictService;

    @Override
    protected FrequencyRestrictService createFrequencyRestrictService() {
        return restrictService;
    }

    @Override
    protected long calculateRunDuration(long durationSeconds) {

        // 访问redis可能存在网络延迟，适当加1秒执行时间
        return durationSeconds + 1L;
    }
}
