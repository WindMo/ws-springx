/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.ssh;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ws.spring.ssh.annotation.SshProxy;

/**
 * @author WindShadow
 * @version 2025-07-06.
 */
@Profile("ssh")
@SshProxy(source = "${spring.ext.ssh.test.proxy.source}",
        localHost = "127.0.0.1",
        localPort = "${spring.ext.ssh.test.proxy.local-port}",
        remoteHost = "${spring.ext.ssh.test.proxy.remote-host}",
        remotePort = "${spring.ext.ssh.test.proxy.remote-port}")
@Configuration(proxyBeanMethods = false)
public class SshProxyConfig {
}
