/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-03-01.
 */
public class SshIOException extends SshException {

    public SshIOException(SshSource source, String message) {
        super(source, message);
    }

    public SshIOException(SshSource source, String message, Throwable cause) {
        super(source, message, cause);
    }
}
