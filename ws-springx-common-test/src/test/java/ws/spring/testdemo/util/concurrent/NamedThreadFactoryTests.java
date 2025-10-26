/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.util.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ws.spring.util.concurrent.NamedThreadFactory;

import java.util.Random;

/**
 * @author WindShadow
 * @version 2024-12-20.
 */
public class NamedThreadFactoryTests {

    private final Random random = new Random();

    @Test
    void newThreadTest() {

        String factoryName = String.format("NamedThreadFactory_%d", random.nextInt());
        String threadNamePrefix = "[" + factoryName + "::thread-";
        NamedThreadFactory factory = new NamedThreadFactory(factoryName);

        int firstCreatedThreadSize = factory.getCurrentCreatedThreadSize();
        int size = random.nextInt(10) + 1;

        for (int i = 0; i < size; i++) {

            Thread thread = factory.newThread(() -> {
            });
            Assertions.assertTrue(thread.getName().startsWith(threadNamePrefix));
        }

        int secondCreatedThreadSize = factory.getCurrentCreatedThreadSize();
        Assertions.assertEquals(firstCreatedThreadSize + size, secondCreatedThreadSize);
    }
}
