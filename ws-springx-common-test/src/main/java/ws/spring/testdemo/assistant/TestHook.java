package ws.spring.testdemo.assistant;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2025-07-17.
 */
@Component
public class TestHook {

    private final Map<String, Consumer<Object>> hooks = new ConcurrentHashMap<>();

    public void addHook(String key, Consumer<Object> consumer) {

        Assert.hasText(key, "The key must not be empty/null");
        Assert.notNull(consumer, "The consumer must not be null");
        hooks.merge(key, consumer, (c1, c2) -> {
            throw new IllegalStateException(String.format("The consume of key: %s is added", key));
        });
    }

    public void callHookIfExist(String key, Object obj) {

        Assert.hasText(key, "The key must not be empty/null");
        Assert.notNull(obj, "The obj must not be null");
        Optional.ofNullable(hooks.get(key))
                .ifPresent(consumer -> consumer.accept(obj));
    }

    public void remmoveHook(String key) {
        hooks.remove(key);
    }
}
