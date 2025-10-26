/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict;

/**
 * @author WindShadow
 * @version 2023-10-18.
 */

public class RestrictorDeclarationException extends RestrictionException {

    public RestrictorDeclarationException(String message) {
        super(message);
    }

    public RestrictorDeclarationException(String message, Throwable cause) {
        super(message, cause);
    }
}
