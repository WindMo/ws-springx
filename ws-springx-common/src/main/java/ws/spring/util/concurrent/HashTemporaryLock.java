package ws.spring.util.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author WindShadow
 * @version 2025-06-08.
 */
public class HashTemporaryLock<E> implements TemporaryLock<E> {

    private static final Object VALUE = new Object();
    private static final BiFunction<? super Object, ? super Object, ?> MERGER = (v1, v2) -> {
        throw new ExclusiveException();
    };

    private final ConcurrentMap<E, Object> identities = new ConcurrentHashMap<>();

    @Override
    public boolean exclusiveRun(E identity, Runnable runnable) {

        try {
            identities.merge(identity, VALUE, MERGER);
            runnable.run();
            return true;
        } catch (ExclusiveException e) {
            return false;
        } finally {
            identities.remove(identity);
        }
    }
}
