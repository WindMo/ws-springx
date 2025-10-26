/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.support.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.Assert;
import redis.embedded.RedisServer;

import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-10-18.
 */

public class RedisServerBean implements InitializingBean, ApplicationRunner {

    private final RedisServer server;

    /** 延迟关闭服务的时间（毫秒） */
    private final int stopTimeout;

    public RedisServerBean(RedisServer server, int stopTimeout) {
        this.server = Objects.requireNonNull(server);
        Assert.isTrue(stopTimeout > 0, "Invalid stopTimeout");
        this.stopTimeout = stopTimeout;
    }

    public void startServer() {
        server.start();
    }

    public void stopServer() {
        server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startServer();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*
               你无法实现bean销毁接口（DisposableBean），然后像下面这样通过注册hook来关闭 redis server，
               Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
               因为spring容器已经注册过一个hook来关闭容器销毁bean，
               当容器在关闭时，说明hook已经在工作，根据ApplicationShutdownHooks源码可知，此时再注册hook已经晚了，也就不会运行了
         */
        /*
                所以我们需要在容器运行之后就注册hook，而不是销毁时注册。
                为了等待redis客户端连接池关闭所有的连接，这里需要等待一定的时间才关闭redis服务
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(stopTimeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                stopServer();
            }
        }));
    }
}
