package ws.spring.util.lambda;

import java.util.function.Supplier;

/**
 * @author WindShadow
 * @version 2024-08-08.
 */
public interface SS<T> extends Supplier<T>, SerializableLambda {
}
