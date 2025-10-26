/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.lang.enums;

import ws.spring.lang.enums.EnumValue;

/**
 * @author WindShadow
 * @version 2022-11-12.
 */

public enum LogEnum implements EnumValue<String> {

    INFO(null),
    ERROR("error");

    private final String value;

    LogEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
