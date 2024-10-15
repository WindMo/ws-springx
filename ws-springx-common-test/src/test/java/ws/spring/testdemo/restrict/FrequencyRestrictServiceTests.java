package ws.spring.testdemo.restrict;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ws.spring.restrict.FrequencyRestrictService;
import ws.spring.restrict.FrequencyRestrictor;
import ws.spring.restrict.FrequencyRestrictorDefinition;
import ws.spring.restrict.RestrictorDeclarationException;
import ws.spring.restrict.support.AbstractFrequencyRestrictor;
import ws.spring.testdemo.SpringxAppTests;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author WindShadow
 * @version 2024-02-24.
 * @see FrequencyRestrictService
 */

@Slf4j
public abstract class FrequencyRestrictServiceTests {

    protected final Random random = new Random();
    private final AtomicInteger number = new AtomicInteger(0);
    protected FrequencyRestrictService frs;

    protected abstract FrequencyRestrictService createFrequencyRestrictService();
    protected abstract long calculateRunDuration(long durationSeconds);

    protected FrequencyRestrictorDefinition randomDefinition() {
        return randomDefinition(1L);
    }

    protected final FrequencyRestrictorDefinition randomDefinition(long duration) {

        FrequencyRestrictorDefinition definition = new FrequencyRestrictorDefinition();
        definition.setName("FrequencyRestrictor(" + number.incrementAndGet() + ")");
        definition.setFrequency(random.nextInt(10) + 1);
        definition.setDuration(duration);
        return definition;
    }

    protected final Runnable buildTrigger(FrequencyRestrictor restrictor, String refer, int frequency) {

        return () -> {

            // 使对refer的频控达到临界
            for (int i = 0; i < frequency; i++) {
                Assertions.assertFalse(restrictor.tryRestrict(refer));
            }
            // FIXME 实际执行依赖频控器（restrictor）的处理速度，此处不一定能控制到refer的频率，即不一定断言成功，得多试几次
            Assertions.assertTrue(restrictor.tryRestrict(refer));
            Assertions.assertFalse(restrictor.tryRestrict(refer + "refer"));
        };
    }

    @BeforeEach
    void beforeTest() {
        frs = createFrequencyRestrictService();
        frs.clearAllRestrictors();
    }

    @Test
    void registerRestrictorTest() {

        FrequencyRestrictorDefinition definition = randomDefinition();
        FrequencyRestrictor restrictor = frs.registerRestrictor(definition);
        Assertions.assertEquals(definition.getName(), restrictor.getName());
        Assertions.assertSame(restrictor, frs.getRestrictor(definition.getName()));
        Exception e = Assertions.assertThrows(RestrictorDeclarationException.class, () -> frs.registerRestrictor(definition));
        log.error(e.getMessage());
    }

    @Test
    void addRestrictorTest() {

        FrequencyRestrictorDefinition definition = randomDefinition();
        FrequencyRestrictor restrictor = frs.registerRestrictor(definition);
        Exception e;
        e = Assertions.assertThrows(RestrictorDeclarationException.class, () -> frs.addRestrictor(restrictor));
        log.error(e.getMessage());

        FrequencyRestrictor unusedRestrictor = new AbstractFrequencyRestrictor(definition.getName()) {

            @Override
            protected boolean doTryRestrict(String refer) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void doResetRestrict(String refer) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void resetRestrictor() {
                throw new UnsupportedOperationException();
            }

        };
        e = Assertions.assertThrows(RestrictorDeclarationException.class, () -> frs.addRestrictor(unusedRestrictor));
        log.error(e.getMessage());
    }

    @Test
    void restrictorTest() throws InterruptedException {

        // 随机的频控器
        long durationSeconds = 1L;
        long runDuration = calculateRunDuration(durationSeconds);
        FrequencyRestrictorDefinition definition = randomDefinition(durationSeconds);
        FrequencyRestrictor restrictor = frs.registerRestrictor(definition);
        int frequency = definition.getFrequency();

        String refer = String.valueOf(random.nextInt(100));

        // 频控测试
        SpringxAppTests.runInDuration(runDuration, TimeUnit.SECONDS, buildTrigger(restrictor, refer, frequency));
        TimeUnit.SECONDS.sleep(runDuration);
        Assertions.assertFalse(restrictor.tryRestrict(refer));

        // 重置测试
        String newRefer = refer + random.nextInt(100);
        SpringxAppTests.runInDuration(runDuration, TimeUnit.SECONDS, buildTrigger(restrictor, newRefer, frequency));
        Assertions.assertTrue(restrictor.tryRestrict(newRefer));
        restrictor.resetRestrict(newRefer);
        Assertions.assertFalse(restrictor.tryRestrict(newRefer));


        // 重置全部
        restrictor.resetRestrict(refer);
        restrictor.resetRestrict(newRefer);
        SpringxAppTests.runInDuration(runDuration, TimeUnit.SECONDS, buildTrigger(restrictor, refer, frequency));
        SpringxAppTests.runInDuration(runDuration, TimeUnit.SECONDS, buildTrigger(restrictor, newRefer, frequency));
        Assertions.assertTrue(restrictor.tryRestrict(refer));
        Assertions.assertTrue(restrictor.tryRestrict(newRefer));
        restrictor.resetRestrictor();
        Assertions.assertFalse(restrictor.tryRestrict(refer));
        Assertions.assertFalse(restrictor.tryRestrict(newRefer));

        // null参数测试
        Exception e = Assertions.assertThrows(IllegalArgumentException.class, () -> restrictor.tryRestrict(null));
        log.error(e.getMessage());
    }
}
