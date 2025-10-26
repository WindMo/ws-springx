/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.config.CustomConfigurationForImport;
import ws.spring.testdemo.config.CustomConfigurationForScan;

/**
 * @author WindShadow
 * @version 2025-06-23.
 */
@Import(CustomConfigurationForImport.class)
public class ExactConfigurationTests extends SpringxAppTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void componentScanTest() {

        Assertions.assertTrue(context.getBeansOfType(CustomConfigurationForScan.class).isEmpty());
        Assertions.assertFalse(context.getBeansOfType(CustomConfigurationForImport.class).isEmpty());
    }
}
