/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.anno;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import ws.spring.context.annotation.YamlSource;
import ws.spring.testdemo.support.redis.DefaultRedisServerFactory;
import ws.spring.testdemo.support.redis.RedisServerBean;
import ws.spring.testdemo.support.redis.RedisServerFactory;
import ws.spring.testdemo.support.redis.RedisServerProperties;

import java.io.IOException;

/**
 * @author WindShadow
 * @version 2024-10-23.
 */
@TestConfiguration(proxyBeanMethods = false)
@ConditionalOnMissingBean({RedisServerFactory.class, RedisServerBean.class})
@YamlSource("classpath:redis/redis-server-config.yml")
@EnableConfigurationProperties(RedisServerProperties.class)
class RedisServerConfiguration {

    @Bean
    public static RedisServerFactory redisServerFactory(RedisServerProperties properties) {
        return new DefaultRedisServerFactory(properties);
    }

    @Bean
    public static RedisServerBean redisServerBean(RedisServerFactory providerFactory, RedisServerProperties properties) throws IOException {
        return new RedisServerBean(providerFactory.buildRedisServer(), properties.getStopTimeout());
    }
}
