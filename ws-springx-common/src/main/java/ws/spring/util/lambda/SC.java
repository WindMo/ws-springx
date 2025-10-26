/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.lambda;

import java.util.function.Consumer;

public interface SC<T> extends Consumer<T>, SerializableLambda {}