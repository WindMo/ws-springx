package ws.spring.util.concurrent;

import org.springframework.lang.Nullable;

import java.util.Objects;
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

    private final ConcurrentMap<IdentityWrapper<E>, Object> identities = new ConcurrentHashMap<>();

    @Override
    public boolean exclusiveRun(@Nullable E identity, Runnable runnable) {

        IdentityWrapper<E> wrapper = IdentityWrapper.wrap(identity);
        try {
            identities.merge(wrapper, VALUE, MERGER);
            runnable.run();
            return true;
        } catch (ExclusiveException e) {
            return false;
        } finally {
            identities.remove(wrapper);
        }
    }

    private static class ExclusiveException extends RuntimeException {}

    private final static class IdentityWrapper<E> {

        private static final IdentityWrapper<?> NULL_WRAPPER = new IdentityWrapper<>(null);

        @Nullable
        private final E identity;
        private final int hash;

        private IdentityWrapper(@Nullable E identity) {
            this.identity = identity;
            this.hash = Objects.hash(identity);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof IdentityWrapper)) return false;
            IdentityWrapper<?> that = (IdentityWrapper<?>) o;
            return Objects.equals(identity, that.identity);
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public String toString() {
            return "identity=" + identity;
        }

        private static <T> IdentityWrapper<T> wrap(@Nullable T identity) {
            return identity == null ? (IdentityWrapper<T>) NULL_WRAPPER : new IdentityWrapper<>(identity);
        }
    }
}
