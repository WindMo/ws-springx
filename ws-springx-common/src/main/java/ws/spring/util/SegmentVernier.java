/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util;

import java.util.List;

@FunctionalInterface
public interface SegmentVernier<E> {

    /**
     * @param offset   offset 从0开始
     * @param interval interval > 0
     * @return elements Not be null
     */
    List<E> peek(int offset, int interval);
}