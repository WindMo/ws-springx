/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import ws.spring.ssh.*;

import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-03-03.
 */
public class DelegateSshService implements SshService {

    private final SshService delegate;

    public DelegateSshService(SshService delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public SshOperator buildSshOperator(String sourceName) {
        return delegate.buildSshOperator(sourceName);
    }

    @Override
    public void registerSshSource(String sourceName, SshSource sshSource) {
        delegate.registerSshSource(sourceName, sshSource);
    }
}
