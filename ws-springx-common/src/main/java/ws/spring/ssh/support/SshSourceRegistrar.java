/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import ws.spring.ssh.SshSourceRegistry;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
public interface SshSourceRegistrar {

    void registerSshSources(SshSourceRegistry registry);
}
