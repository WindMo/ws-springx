package ws.spring.util.lambda;

import java.util.function.Predicate;

/**
 * @author WindShadow
 * @version 2024-08-08.
 */
public interface SP<T> extends Predicate<T>, SerializableLambda{
}
