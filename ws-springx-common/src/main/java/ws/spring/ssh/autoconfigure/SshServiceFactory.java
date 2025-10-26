/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.autoconfigure;

import ws.spring.ssh.SshDuration;
import ws.spring.ssh.SshService;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
public interface SshServiceFactory {

    SshService buildSshService(SshDuration sshDuration);
}
