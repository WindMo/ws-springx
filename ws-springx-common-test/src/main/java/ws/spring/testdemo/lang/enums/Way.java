/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.lang.enums;

import ws.spring.lang.enums.EnumValue;

/**
 * @author WindShadow
 * @version 2022-02-19.
 */

public enum Way implements EnumValue<Integer> {

    Up(1);

    private Integer code;

    Way(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
