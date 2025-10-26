/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.service;

import ws.spring.restrict.annotation.FrequencyRestrict;

/**
 * @author WindShadow
 * @version 2024-02-24.
 */
public interface INeedRestrictService {

    @FrequencyRestrict
    void access();
}
