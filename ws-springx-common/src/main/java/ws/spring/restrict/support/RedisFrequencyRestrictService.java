package ws.spring.restrict.support;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import ws.spring.restrict.FrequencyRestrictor;
import ws.spring.restrict.FrequencyRestrictorDefinition;
import ws.spring.restrict.RestrictorDeclarationException;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于Redis实现的频控服务
 *
 * @author WindShadow
 * @version 2024-10-15.
 */
public class RedisFrequencyRestrictService extends GenericFrequencyRestrictService {

    private final RedisOperations<String, String> redisOperations;

    public RedisFrequencyRestrictService(RedisOperations<String, String> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @Override
    protected FrequencyRestrictor createRestrictor(FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException {
        return new RedisFrequencyRestrictor(definition, redisOperations.opsForValue());
    }

    protected static class RedisFrequencyRestrictor extends GenericFrequencyRestrictor {

        private final Lock mainLock = new ReentrantLock();
        private static final String KEY_FORMAT = "$Restrictor.%s.%s.T%s";
        private static final String DEFAULT_VALUE = "";

        private final ValueOperations<String, String> valueOps;
        private final String restrictorKeyPattern;

        protected RedisFrequencyRestrictor(FrequencyRestrictorDefinition definition, ValueOperations<String, String> valueOps) {
            super(definition);
            this.valueOps = valueOps;
            this.restrictorKeyPattern = generateRedisKeyPattern("*");
        }

        protected final String generateRedisKey(String refer, long timeMillis) {
            return String.format(KEY_FORMAT, name, refer, timeMillis);
        }

        protected final String generateRedisKeyPattern(String refer) {
            return String.format(KEY_FORMAT, name, refer, "*");
        }

        protected final void deleteForKeyPatten(String keyPattern) {

            Set<String> referKeys = Optional.ofNullable(valueOps.getOperations().keys(keyPattern))
                    .orElseThrow(() -> new IllegalStateException("The current operation of redis is in a transaction"));
            valueOps.getOperations().delete(referKeys);
        }

        protected final int countRefer(String refer) {

            String referKeyPatten = generateRedisKeyPattern(refer);
            return Optional.ofNullable(valueOps.getOperations().keys(referKeyPatten))
                    .map(Set::size)
                    .orElseThrow(() -> new IllegalStateException("The current operation of redis is in a transaction"));
        }

        @Override
        public void resetRestrictor() {
            deleteForKeyPatten(restrictorKeyPattern);
        }

        @Override
        protected boolean doTryRestrict(String refer) {

            int count = countRefer(refer);
            if (count < frequency) {

                mainLock.lock();
                try {
                    if ((count = countRefer(refer)) < frequency) {
                        String referKey = generateRedisKey(refer, System.currentTimeMillis());
                        valueOps.set(referKey, DEFAULT_VALUE, durationMillis, TimeUnit.MILLISECONDS);
                        return false;
                    }
                } finally {
                    mainLock.unlock();
                }
            }
            if (count == frequency){
                return true;
            } else {
                throw new IllegalStateException(String.format("The count[%d] of refer[%s] exceeds frequency[%d]", count, refer, frequency));
            }
        }

        @Override
        protected void doResetRestrict(String refer) {

            String referKeyPatten = generateRedisKeyPattern(refer);
            deleteForKeyPatten(referKeyPatten);
        }
    }
}
