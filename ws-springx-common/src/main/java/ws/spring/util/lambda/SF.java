package ws.spring.util.lambda;

import java.util.function.Function;

/**
 * @author WindShadow
 * @version 2024-08-08.
 */
public interface SF<T, R> extends Function<T, R>, SerializableLambda {
}
