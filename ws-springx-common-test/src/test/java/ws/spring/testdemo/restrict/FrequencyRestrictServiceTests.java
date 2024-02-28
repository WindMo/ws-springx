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

    @BeforeEach
    void beforeTest() {
        frs = createFrequencyRestrictService();
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

        FrequencyRestrictor unusedRestrictor = new AbstractFrequencyRestrictor(definition.getName(), false) {
            @Override
            protected boolean doTryRestrict(String refer) {
                throw new UnsupportedOperationException();
            }
        };
        e = Assertions.assertThrows(RestrictorDeclarationException.class, () -> frs.addRestrictor(unusedRestrictor));
        log.error(e.getMessage());
    }

    @Test
    void restrictorTest() throws InterruptedException {

        long duration = 1L;
        FrequencyRestrictorDefinition definition = randomDefinition(duration);
        FrequencyRestrictor restrictor = frs.registerRestrictor(definition);
        int frequency = definition.getFrequency();
        String refer = String.valueOf(random.nextInt(100));
        SpringxAppTests.runInDuration(duration, TimeUnit.SECONDS, () -> {
            for (int i = 0; i < frequency; i++) {
                Assertions.assertFalse(restrictor.tryRestrict(refer));
            }
            Assertions.assertTrue(restrictor.tryRestrict(refer));
            Assertions.assertFalse(restrictor.tryRestrict(refer + "new"));
        });
        TimeUnit.SECONDS.sleep(duration);
        Assertions.assertFalse(restrictor.tryRestrict(refer));
        Exception e = Assertions.assertThrows(IllegalArgumentException.class, () -> restrictor.tryRestrict(null));
        log.error(e.getMessage());
    }
}
