/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.rest;

import org.springframework.lang.Nullable;
import ws.spring.web.rest.response.RestCodeSupplier;
import ws.spring.web.rest.response.RestResponse;

/**
 * @author WindShadow
 * @version 2022-06-12.
 */

public enum GlobalRest implements RestCodeSupplier {

    SUCCESS("WS.Springx.0000", "success"),
    FAILED("WS.Springx.5000", "failed");

    private final String code;

    private final String defaultMessage;

    GlobalRest(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    public <T> RestResponse<T> empty() {

        return of(null);
    }

    public <T> RestResponse<T> of(@Nullable T data) {

        return of(this.defaultMessage, data);
    }

    public <T> RestResponse<T> of(String message, @Nullable T data) {

        return RestResponse.of(this, message, data);
    }
}
