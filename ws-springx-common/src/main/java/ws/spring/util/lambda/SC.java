package ws.spring.util.lambda;

import java.util.function.Consumer;

public interface SC<T> extends Consumer<T>, SerializableLambda {}