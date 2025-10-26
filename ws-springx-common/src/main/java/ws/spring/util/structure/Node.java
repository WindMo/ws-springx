/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.structure;

import org.springframework.lang.Nullable;

/**
 * @author WindShadow
 * @version 2023-06-19.
 */

public interface Node<E> {

    @Nullable
    E getIdentity();
}
