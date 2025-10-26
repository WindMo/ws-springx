/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.mybatis.mapper;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */
class MapperRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {

        Optional.ofNullable(AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ImportMapper.class.getName())))
                .ifPresent(importMapperAttrs -> this.registerBeanDefinitions(importingClassMetadata, importMapperAttrs, registry));

        Optional.ofNullable(AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ImportMapper.List.class.getName())))
                .map(attrs -> attrs.getAnnotationArray("value"))
                .ifPresent(importMapperAttrsArray -> {
                    for (AnnotationAttributes importMapperAttrs : importMapperAttrsArray) {
                        this.registerBeanDefinitions(importingClassMetadata, importMapperAttrs, registry);
                    }
                });
    }

    private void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, AnnotationAttributes importMapperAttrs, BeanDefinitionRegistry registry) {

        Class<?> mapperInterface = importMapperAttrs.getClass("mapper");
        String beanName = generateMapperBeanName(importingClassMetadata, mapperInterface);
        Assert.state(!registry.containsBeanDefinition(beanName), String.format("Register the mapper [%s] repeatedly", mapperInterface));

        String location = importMapperAttrs.getString("location");
        String mapperLocation = location.isEmpty() ? null : location;
        String sqlSessionFactoryRef = importMapperAttrs.getString("sqlSessionFactoryRef");

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(EnhancedMapperFactory.class)
                .addConstructorArgValue(mapperInterface)
                .addPropertyValue("mapperLocation", mapperLocation);
        if (sqlSessionFactoryRef.isEmpty()) {
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        } else {
            builder.addPropertyReference("sqlSessionFactory", sqlSessionFactoryRef);
        }
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static String generateMapperBeanName(AnnotationMetadata importingClassMetadata, Class<?> mapper) {
        return importingClassMetadata.getClassName() + "#" + MapperRegistrar.class.getSimpleName() + "#" + mapper.getSimpleName();
    }
}
