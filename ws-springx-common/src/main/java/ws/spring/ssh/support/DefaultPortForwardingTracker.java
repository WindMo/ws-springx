/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import ws.spring.ssh.PortForwardingTracker;

import java.util.function.Predicate;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */
public class DefaultPortForwardingTracker<T extends AutoCloseable> implements PortForwardingTracker {

    private final String localHost;
    private final int localPort;

    private final String remoteHost;
    private final int remotePort;
    private final T closeable;
    private final Predicate<T> predicate;

    public DefaultPortForwardingTracker(String localHost, int localPort, String remoteHost, int remotePort, T closeable, Predicate<T> predicate) {
        this.localHost = localHost;
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.closeable = closeable;
        this.predicate = predicate;
    }

    @Override
    public String getLocalHost() {
        return localHost;
    }

    @Override
    public int getLocalPort() {
        return localPort;
    }

    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    @Override
    public boolean isClosed() {
        return predicate.test(closeable);
    }

    @Override
    public void close() throws Exception {
        closeable.close();
    }
}
