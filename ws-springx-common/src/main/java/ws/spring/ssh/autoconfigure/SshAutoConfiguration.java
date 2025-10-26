/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;
import ws.spring.ssh.SshService;
import ws.spring.ssh.support.SshForwardDeclaration;
import ws.spring.ssh.support.SshForwarder;
import ws.spring.ssh.support.SshServiceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.ext.ssh", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SshConfigProperties.class)
public class SshAutoConfiguration {

    @Bean
    public static SshConfig sshConfig(SshConfigProperties properties) {
        return new SshConfig(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public static SshService sshService(SshConfig config, SshServiceFactory factory) throws Exception {

        SshService service = new SshServiceBean(factory.buildSshService(config.getSshDuration()));
        for (Map.Entry<String, SshSourceProperties> entry : config.getAuthIdentities().entrySet()) {

            String name = entry.getKey();
            SshSourceProperties identity = entry.getValue();
            service.registerSshSource(name, identity.buildSshSource());
        }
        return service;
    }

    @Bean
    public static SshForwarderComposite sshForwarderRegistrar(SshConfig config, SshService service) {

        List<SshForwarder> forwarders = new ArrayList<>();
        for (Map.Entry<String, SshSourceProperties> entry : config.getAuthIdentities().entrySet()) {

            SshSourceProperties identity = entry.getValue();

            List<SshForwardDeclaration> forwards = identity.getForwards();
            if (CollectionUtils.isEmpty(forwards)) continue;
            String sshSourceName = entry.getKey();
            SshForwarder forwarder = new SshForwarder(sshSourceName, service.buildSshOperator(sshSourceName));
            for (SshForwardDeclaration declaration : forwards) {
                forwarder.addForwardDeclaration(declaration);
            }
            forwarders.add(forwarder);
        }
        return new SshForwarderComposite(forwarders);
    }
}
