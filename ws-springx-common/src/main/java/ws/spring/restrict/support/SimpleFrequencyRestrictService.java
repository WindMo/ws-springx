package ws.spring.restrict.support;

import ws.spring.restrict.FrequencyRestrictor;
import ws.spring.restrict.FrequencyRestrictorDefinition;
import ws.spring.restrict.RestrictorDeclarationException;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author WindShadow
 * @version 2024-01-26.
 */

public class SimpleFrequencyRestrictService extends AbstractFrequencyRestrictService {

    private final Map<String, FrequencyRestrictor> restrictors = new ConcurrentHashMap<>(16);

    @Override
    protected FrequencyRestrictor createRestrictor(FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException {
        return new BlockingFrequencyRestrictor(definition);
    }

    @Override
    protected boolean hasRestrictor(String restrictorName) {
        return restrictors.containsKey(restrictorName);
    }

    @Override
    protected void doAddRestrictor(FrequencyRestrictor restrictor) {
        restrictors.putIfAbsent(restrictor.getName(), restrictor);
    }

    @Override
    public FrequencyRestrictor getRestrictor(String restrictorName) {
        return restrictors.get(restrictorName);
    }

    protected static class BlockingFrequencyRestrictor extends AbstractFrequencyRestrictor {

        private final int frequency;
        private final long durationMillis;

        /**
         * key为refer，队列元素为毫秒时间戳
         */
        private final Map<String, BlockingQueue<Long>> referQueues;

        protected BlockingFrequencyRestrictor(FrequencyRestrictorDefinition definition) {
            super(definition.getName(), false);
            this.frequency = definition.getFrequency();
            this.durationMillis = TimeUnit.SECONDS.toMillis(definition.getDuration());
            this.referQueues = new ConcurrentHashMap<>();
        }

        @Override
        protected boolean doTryRestrict(String refer) {

            BlockingQueue<Long> queue = referQueues.computeIfAbsent(refer, r -> new ArrayBlockingQueue<>(frequency));
            long currentTimeMillis = System.currentTimeMillis();
            if (queue.offer(currentTimeMillis)) return false;
            // 如果队列已满，则尝试移除旧时间戳
            while (!queue.isEmpty() && (queue.peek() + durationMillis <= currentTimeMillis)) queue.remove();
            return !queue.offer(currentTimeMillis);
        }

        @Override
        protected void doResetRestrict(String refer) {
            referQueues.remove(refer);
        }

        @Override
        public void resetRestrictor() {
            referQueues.clear();
        }
    }
}
