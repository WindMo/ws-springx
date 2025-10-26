/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.support.redis;

import redis.embedded.RedisServer;

/**
 * @author WindShadow
 * @version 2024-10-23.
 */
public interface RedisServerFactory {

    RedisServer buildRedisServer();
}
