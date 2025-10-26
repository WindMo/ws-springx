/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.restrict;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ws.spring.restrict.RestrictedCriticalException;

import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.service.NeedRestrictService;

import java.util.concurrent.TimeUnit;

/**
 * @author WindShadow
 * @version 2024-02-24.
 */
@Slf4j
public class FrequencyRestrictTests extends SpringxAppTests {

    @Autowired
    private NeedRestrictService service;

    @Test
    void staticReferTest() throws InterruptedException {

        runInDuration(1L, TimeUnit.SECONDS, () -> {

            Assertions.assertDoesNotThrow(() -> service.access());
            Exception e = Assertions.assertThrows(RestrictedCriticalException.class, () -> service.access());
            log.error(e.getMessage());
        });
        TimeUnit.SECONDS.sleep(1L);
        Assertions.assertDoesNotThrow(() -> service.access());
    }

    @Test
    void dynamicReferTest() throws InterruptedException {

        String name = "tom";
        runInDuration(1L, TimeUnit.SECONDS, () -> {

            Assertions.assertDoesNotThrow(() -> service.access(name));
            Exception e = Assertions.assertThrows(RestrictedCriticalException.class, () -> service.access(name));
            log.error(e.getMessage());
            Assertions.assertDoesNotThrow(() -> service.access(name + "-jerry"));
        });
        TimeUnit.SECONDS.sleep(1L);
        Assertions.assertDoesNotThrow(() -> service.access(name));
    }
}
