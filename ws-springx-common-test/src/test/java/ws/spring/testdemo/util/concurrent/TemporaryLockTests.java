/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.util.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ws.spring.util.concurrent.HashTemporaryLock;
import ws.spring.util.concurrent.TemporaryLock;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author WindShadow
 * @version 2025-06-09.
 */
public class TemporaryLockTests {

    private final Random random = new Random();
    private int size;
    private ExecutorService executor;
    private TemporaryLock<String> lock;

    private volatile boolean running = false;
    private volatile String key = "key";

    @BeforeEach
    void before() {

        size = random.nextInt(5) + 2;
        lock = new HashTemporaryLock<>();
        executor = Executors.newFixedThreadPool(size);
    }

    @Test
    void exclusiveRunTest() throws ExecutionException, InterruptedException {

        Set<Long> threadIds = ConcurrentHashMap.newKeySet();
        Runnable task = () -> {

            Assertions.assertFalse(running);
            running = true;
            threadIds.add(Thread.currentThread().getId());
            running = false;
        };


        CountDownLatch cdl = new CountDownLatch(size);
        Runnable runner = () -> {

            cdl.countDown();
            try {
                cdl.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            if (lock.exclusiveRun(key, task)) {
                Assertions.assertTrue(threadIds.contains(Thread.currentThread().getId()));
            } else {
                Assertions.assertFalse(threadIds.contains(Thread.currentThread().getId()));
            }
        };

        CompletableFuture<?>[] futures = new CompletableFuture[size];
        for (int i = 0; i < size; i++) {
            futures[i] = CompletableFuture.runAsync(runner, executor);
        }

        CompletableFuture.allOf(futures).get();

        if (key != null) {
            key = null;
            exclusiveRunTest();
        }
    }
}
