/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.concurrent;

import org.springframework.lang.Nullable;

/**
 * 临时独占锁
 *
 * @author WindShadow
 * @version 2025-06-02.
 */
public interface TemporaryLock<E> {

    /**
     * 以 identity 为锁，多次调用此方法时，保证相同的identity，同时只有一个线程调用成功，且执行对应的任务
     *
     * @param identity
     * @param runnable 任务
     * @return 被排斥时返回false，反之为true
     */
    boolean exclusiveRun(@Nullable E identity, Runnable runnable);
}
