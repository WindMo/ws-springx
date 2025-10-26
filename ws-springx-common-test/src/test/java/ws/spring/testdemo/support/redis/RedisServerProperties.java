/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.support.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import redis.embedded.util.Architecture;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.net.InetSocketAddress;

/**
 * @author WindShadow
 * @version 2024-10-23.
 */
@Data
@Validated
@ConfigurationProperties(prefix = "springx.test.redis")
public class RedisServerProperties {

    private int port;

    @Min(1)
    private int stopTimeout = 3000;

    @Nullable
    private String configFileLocation;

    @Nullable
    private InetSocketAddress slaveOf;

    @Valid
    @Nullable
    private RedisServerExecutable windows;

    @Valid
    @Nullable
    private RedisServerExecutable unix;

    @Valid
    @Nullable
    private RedisServerExecutable mac;

    @Data
    static class RedisServerExecutable {

        @Nullable
        private Architecture architecture;

        @NotBlank
        private String location;
    }
}
