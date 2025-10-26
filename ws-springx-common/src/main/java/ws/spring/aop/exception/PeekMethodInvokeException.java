/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop.exception;

/**
 * @author WindShadow
 * @version 2022-02-10.
 */

public class PeekMethodInvokeException extends RuntimeException {

    public PeekMethodInvokeException(String message) {
        super(message);
    }

    public PeekMethodInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PeekMethodInvokeException(Throwable cause) {
        super(cause);
    }

    public PeekMethodInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
