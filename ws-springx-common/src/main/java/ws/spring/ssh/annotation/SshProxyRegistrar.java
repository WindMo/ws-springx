/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.annotation;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author WindShadow
 * @version 2025-06-30.
 */
class SshProxyRegistrar implements ImportBeanDefinitionRegistrar {

    private static final AtomicInteger FORWARDER_FACTORY_ID = new AtomicInteger(0);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator) {

        metadata.getAnnotations()
                .stream(SshProxy.class)
                .forEach(proxy -> registerForwarder(proxy, metadata, registry));

        metadata.getAnnotations()
                .stream(SshProxy.List.class)
                .map(anno -> anno.getAnnotationArray("value", SshProxy.class))
                .flatMap(Stream::of)
                .forEach(proxy -> registerForwarder(proxy, metadata, registry));
    }

    private void registerForwarder(MergedAnnotation<SshProxy> proxy, AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        AbstractBeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(SshForwarderFactory.class)
                .addPropertyValue("sshSourceName", proxy.getString("source"))
                .addPropertyValue("localHostConfig", proxy.getString("localHost"))
                .addPropertyValue("localPortConfig", proxy.getString("localPort"))
                .addPropertyValue("remoteHostConfig", proxy.getString("remoteHost"))
                .addPropertyValue("remotePortConfig", proxy.getString("remotePort"))
                .setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE)
                .getBeanDefinition();
        String beanName = metadata.getClassName() + "#" + FORWARDER_FACTORY_ID.incrementAndGet();
        registry.registerBeanDefinition(beanName, definition);
    }
}
