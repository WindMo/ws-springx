/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshOperations;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WindShadow
 * @version 2025-06-30.
 */
public class SshForwarder {

    protected final Log log = LogFactory.getLog(getClass());

    private final String name;

    private final SshOperations operations;

    private final List<PortForwardingTracker> trackers = new CopyOnWriteArrayList<>();

    public SshForwarder(String name, SshOperations operations) {
        this.name = Objects.requireNonNull(name);
        this.operations = Objects.requireNonNull(operations);
    }

    public void addForwardDeclaration(SshForwardDeclaration declaration) {

        Assert.notNull(declaration, "The forward declaration must not be null");
        String localHost = declaration.getLocalHost();
        int localPort = declaration.getLocalPort();
        String remoteHost = declaration.getRemoteHost();
        int remotePort = declaration.getRemotePort();
        Assert.hasText(localHost, "The local host is not set");
        Assert.isTrue(localPort > 0L, "The local port is invalid");
        Assert.hasText(remoteHost, "The remote host is not set");
        Assert.isTrue(remotePort > 0L, "The remote port is invalid");
        PortForwardingTracker tracker = operations.forward(localHost, localPort, remoteHost, remotePort);
        trackers.add(tracker);
    }

    public void destroyTrackers() {

        Iterator<PortForwardingTracker> iterator = trackers.iterator();
        while (iterator.hasNext()) {

            PortForwardingTracker tracker = iterator.next();
            try {
                tracker.close();
            } catch (Exception e) {
                log.error(String.format("Error while closing tracker[%s:%d -> %s:%d]",
                        tracker.getLocalHost(), tracker.getLocalPort(), tracker.getRemoteHost(), tracker.getRemotePort()));
            } finally {
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "SshForwarder{" +
                "name='" + name + '\'' +
                '}';
    }
}
