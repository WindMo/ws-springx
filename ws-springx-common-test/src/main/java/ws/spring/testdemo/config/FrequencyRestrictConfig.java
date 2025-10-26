/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import ws.spring.restrict.FrequencyRestrictService;
import ws.spring.restrict.support.RedisFrequencyRestrictService;

/**
 * @author WindShadow
 * @version 2024-10-17.
 */
@ConditionalOnProperty(prefix = "app.", name = "frequency-restrictor", havingValue = "redis")
@Configuration(proxyBeanMethods = false)
public class FrequencyRestrictConfig {

    @Bean
    public static FrequencyRestrictService frequencyRestrictService(RedisOperations<String, String> redisOperations) {

        return new RedisFrequencyRestrictService(redisOperations);
    }
}
