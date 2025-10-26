/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.lang.enums;

import ws.spring.lang.enums.EnumValue;

/**
 * @author WindShadow
 * @version 2022-11-13.
 */

public enum OperateSystem implements EnumValue<String> {

    Windows("os"),
    Linux("os");

    private final String name;

    OperateSystem(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name;
    }
}
