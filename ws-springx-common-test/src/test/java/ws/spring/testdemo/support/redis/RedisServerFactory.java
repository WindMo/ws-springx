package ws.spring.testdemo.support.redis;

import redis.embedded.RedisServer;

/**
 * @author WindShadow
 * @version 2024-10-23.
 */
public interface RedisServerFactory {

    RedisServer buildRedisServer();
}
