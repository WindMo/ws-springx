package ws.spring.testdemo.support;

import org.springframework.beans.factory.annotation.Value;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author WindShadow
 * @version 2024-10-18.
 */

public class RedisServerSupport {

    private final RedisServer server;

    public RedisServerSupport(@Value("${spring.redis.port:6379}") int port,
                              @Value("${app.test.redis.config-line}") String configLine) {

        this.server = RedisServer.builder()
                .port(port)
                .setting(configLine)
                .build();
    }

    @PostConstruct
    public void startRedisServer() {
        server.start();
    }

    @PreDestroy
    public void stopRedisServer() {
        server.stop();
    }
}
