/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.support.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;
import redis.embedded.util.Architecture;
import redis.embedded.util.OS;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author WindShadow
 * @version 2024-10-23.
 */
public class DefaultRedisServerFactory implements RedisServerFactory, InitializingBean {

    private final RedisServerProperties properties;
    private final RedisExecProvider provider;

    public DefaultRedisServerFactory(RedisServerProperties properties) {
        this.properties = Objects.requireNonNull(properties);
        this.provider = RedisExecProvider.defaultProvider();
    }

    @Override
    public RedisServer buildRedisServer() {

        RedisServerBuilder builder = RedisServer.builder()
                .redisExecProvider(provider)
                .port(properties.getPort());
        Optional.ofNullable(properties.getConfigFileLocation())
                .map(DefaultRedisServerFactory::findResourceLocationPath)
                .ifPresent(builder::configFile);
        Optional.ofNullable(properties.getSlaveOf())
                .ifPresent(builder::slaveOf);
        return builder.build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        overrideExecutable(provider, OS.WINDOWS, properties.getWindows());
        overrideExecutable(provider, OS.UNIX, properties.getUnix());
        overrideExecutable(provider, OS.MAC_OS_X, properties.getMac());
    }

    static void overrideExecutable(RedisExecProvider provider, OS os, @Nullable RedisServerProperties.RedisServerExecutable executable) throws FileNotFoundException {

        if (executable == null) return;

        Architecture architecture = executable.getArchitecture();
        String path = findResourceLocationPath(executable.getLocation());
        if (architecture == null) {
            provider.override(os, path);
        } else {
            provider.override(os, architecture, path);
        }
    }

    static String findResourceLocationPath(String location) {

        try {
            return ResourceUtils.getFile(Objects.requireNonNull(location)).getAbsolutePath();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
