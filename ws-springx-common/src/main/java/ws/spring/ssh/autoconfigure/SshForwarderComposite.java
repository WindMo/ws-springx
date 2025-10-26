/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.autoconfigure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.CollectionUtils;
import ws.spring.ssh.support.SshForwarder;

import java.util.List;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2025-07-06.
 */
class SshForwarderComposite implements DisposableBean {

    protected final Log log = LogFactory.getLog(getClass());

    private final List<SshForwarder> forwarders;

    SshForwarderComposite(List<SshForwarder> forwarders) {
        this.forwarders = Objects.requireNonNull(forwarders);
    }

    @Override
    public void destroy() throws Exception {

        if (CollectionUtils.isEmpty(forwarders)) return;
        for (SshForwarder forwarder : forwarders) {
            try {
                forwarder.destroyTrackers();
            } catch (Exception e) {
                log.error(String.format("Error while destroying SshForwarder[%s]", forwarder));
            }
        }
    }
}
