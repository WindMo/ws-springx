/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ws.spring.restrict.annotation.FrequencyRestrict;

/**
 * @author WindShadow
 * @version 2024-02-24.
 */

@Slf4j
@Service
public class NeedRestrictService implements INeedRestrictService {

    @Override
    public void access() {
        log.info("call access");
    }

    @FrequencyRestrict(refer = "T(java.lang.String).valueOf(#name)")
    public void access(String name) {
        log.info("call access. name: {}", name);
    }
}
