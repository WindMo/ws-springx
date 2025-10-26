/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.context.annotation;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AliasFor;
import ws.spring.io.support.YamlPropertySourceFactory;

import java.lang.annotation.*;

/**
 * 可通过该注解引入一个yaml配置文件，通常在配置类上使用
 *
 * @author WindShadow
 * @version 2024-10-24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PropertySource(value = "", factory = YamlPropertySourceFactory.class)
public @interface YamlSource {

    @AliasFor(annotation = PropertySource.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = PropertySource.class, attribute = "value")
    String[] value();

    @AliasFor(annotation = PropertySource.class, attribute = "ignoreResourceNotFound")
    boolean ignoreResourceNotFound() default false;

    @AliasFor(annotation = PropertySource.class, attribute = "encoding")
    String encoding() default "";
}
