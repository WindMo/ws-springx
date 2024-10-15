package ws.spring.testdemo.restrict;

import ws.spring.restrict.FrequencyRestrictService;
import ws.spring.restrict.support.SimpleFrequencyRestrictService;

/**
 * @author WindShadow
 * @version 2024-02-24.
 */
public class SimpleFrequencyRestrictServiceTests extends FrequencyRestrictServiceTests {

    @Override
    protected FrequencyRestrictService createFrequencyRestrictService() {
        return new SimpleFrequencyRestrictService();
    }

    @Override
    protected long calculateRunDuration(long durationSeconds) {
        return durationSeconds;
    }
}
