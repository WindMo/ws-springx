/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.controller.advice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ws.spring.web.bind.FormModelResolver;
import ws.spring.web.bind.RestProxyResolver;

import java.util.List;

/**
 * @author WindShadow
 * @version 2022-07-05.
 */
@Configuration
public class PracticalWebBindSupport implements WebMvcConfigurer {

    private final RestTemplateBuilder builder;

    public PracticalWebBindSupport(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new FormModelResolver());
        resolvers.add(new RestProxyResolver(builder));
    }
}
