/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.autoconfigure;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSessionCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@ConditionalOnClass(SshClient.class)
@AutoConfigureBefore(SshAutoConfiguration.class)
@ConditionalOnMissingBean(SshServiceFactory.class)
public class MinaSshAutoConfiguration {

    @Bean
    public static SshServiceFactory sshServiceFactory(ClientSessionCreator client) {
        return new MinaSshServiceFactory(client);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public static SshClient defaultClient() {

        SshClient client = SshClient.setUpDefaultClient();
        client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
        return client;
    }
}