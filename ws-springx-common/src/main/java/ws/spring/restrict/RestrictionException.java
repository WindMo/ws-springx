/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict;

/**
 * @author WindShadow
 * @version 2023-10-18.
 */

public abstract class RestrictionException extends RuntimeException {

    public RestrictionException(String message) {
        super(message);
    }

    public RestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
