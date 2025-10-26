/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import ws.spring.constant.NetworkConstants;

/**
 * @author WindShadow
 * @version 2025-06-30.
 */
public class SshForwardDeclaration {

    private String localHost = NetworkConstants.IP_V4_ANY_ADDR;
    private int localPort = NetworkConstants.RANDOM_PORT;
    private String remoteHost;
    private int remotePort;

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public String toString() {
        return String.format("SshForwardDeclaration[%s:%d -> %s:%d]", localHost, localPort, remoteHost, remotePort);
    }
}
