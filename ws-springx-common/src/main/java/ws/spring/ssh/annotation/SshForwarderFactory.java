/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.annotation;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;
import ws.spring.ssh.SshOperator;
import ws.spring.ssh.SshService;
import ws.spring.ssh.support.SshForwardDeclaration;
import ws.spring.ssh.support.SshForwarderBean;

import java.util.Optional;

/**
 * @author WindShadow
 * @version 2025-07-06.
 */
class SshForwarderFactory implements FactoryBean<SshForwarderBean>, EmbeddedValueResolverAware {

    private StringValueResolver resolver;

    private String sshSourceName;

    private SshService sshService;

    private String localHostConfig;

    private String localPortConfig;

    private String remoteHostConfig;

    private String remotePortConfig;

    public String getSshSourceName() {
        return sshSourceName;
    }

    public void setSshSourceName(String sshSourceName) {
        this.sshSourceName = sshSourceName;
    }

    public SshService getSshService() {
        return sshService;
    }

    public void setSshService(SshService sshService) {
        this.sshService = sshService;
    }

    public String getLocalHostConfig() {
        return localHostConfig;
    }

    public void setLocalHostConfig(String localHostConfig) {
        this.localHostConfig = localHostConfig;
    }

    public String getLocalPortConfig() {
        return localPortConfig;
    }

    public void setLocalPortConfig(String localPortConfig) {
        this.localPortConfig = localPortConfig;
    }

    public String getRemoteHostConfig() {
        return remoteHostConfig;
    }

    public void setRemoteHostConfig(String remoteHostConfig) {
        this.remoteHostConfig = remoteHostConfig;
    }

    public String getRemotePortConfig() {
        return remotePortConfig;
    }

    public void setRemotePortConfig(String remotePortConfig) {
        this.remotePortConfig = remotePortConfig;
    }

    @Override
    public SshForwarderBean getObject() throws Exception {

        Assert.state(StringUtils.hasText(sshSourceName), "The sshSourceName not set");
        Assert.state(sshService != null, "The sshService not set");


        String localHost = Optional.ofNullable(localHostConfig)
                .map(resolver::resolveStringValue)
                .orElseThrow(() -> new IllegalArgumentException("The localHost config is invalid"));

        int localPort = Optional.ofNullable(localPortConfig)
                .map(resolver::resolveStringValue)
                .map(Integer::parseInt)
                .orElseThrow(() -> new IllegalArgumentException("The localPort config is invalid"));

        String remoteHost = Optional.ofNullable(remoteHostConfig)
                .map(resolver::resolveStringValue)
                .orElseThrow(() -> new IllegalArgumentException("The remoteHost config is invalid"));

        int remotePort = Optional.ofNullable(remotePortConfig)
                .map(resolver::resolveStringValue)
                .map(Integer::parseInt)
                .orElseThrow(() -> new IllegalArgumentException("The remotePort config is invalid"));

        SshForwardDeclaration declaration = new SshForwardDeclaration();
        declaration.setLocalHost(localHost);
        declaration.setLocalPort(localPort);
        declaration.setRemoteHost(remoteHost);
        declaration.setRemotePort(remotePort);

        SshOperator operator = sshService.buildSshOperator(sshSourceName);
        SshForwarderBean forwarder = new SshForwarderBean(sshSourceName, operator);
        forwarder.addForwardDeclaration(declaration);
        return forwarder;
    }

    @Override
    public Class<SshForwarderBean> getObjectType() {
        return SshForwarderBean.class;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }
}
