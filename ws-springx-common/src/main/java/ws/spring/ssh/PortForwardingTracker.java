/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */
public interface PortForwardingTracker extends AutoCloseable {

    String getLocalHost();

    int getLocalPort();

    String getRemoteHost();

    int getRemotePort();

    boolean isClosed();
}
