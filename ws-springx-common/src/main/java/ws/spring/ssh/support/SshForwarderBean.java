/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import org.springframework.beans.factory.DisposableBean;
import ws.spring.ssh.SshOperations;

/**
 * @author WindShadow
 * @version 2025-07-06.
 */
public class SshForwarderBean extends SshForwarder implements DisposableBean {

    public SshForwarderBean(String name, SshOperations operations) {
        super(name, operations);
    }

    @Override
    public void destroy() throws Exception {
        destroyTrackers();
    }
}
