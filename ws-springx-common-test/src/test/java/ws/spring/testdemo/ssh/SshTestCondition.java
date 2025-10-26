/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.ssh;

import org.springframework.core.io.ClassPathResource;
import ws.spring.util.NetworkUtils;
import ws.spring.util.YamlLoaderUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

/**
 * @author WindShadow
 * @version 2024-10-05.
 */
public class SshTestCondition {

    private static final String CONFIG_RESOURCE_LOCATION = "application-ssh.yml";
    private static final String[] SSH_USE = {SshTests.MAIN_SSH_SOURCE, SshTests.MAIN_KEY_SSH_SOURCE};
    private static final Set<String> ADDRESSES = new HashSet<>();

    static {

        Properties properties = YamlLoaderUtils.yamlToProperties(new ClassPathResource(CONFIG_RESOURCE_LOCATION));
        for (String name : SSH_USE) {

            Optional.ofNullable(properties.getProperty(String.format("spring.ext.ssh.accounts.-[%s].host", name)))
                            .ifPresent(ADDRESSES::add);
            Optional.ofNullable(properties.getProperty(String.format("spring.ext.ssh.key-pairs.-[%s].host", name)))
                            .ifPresent(ADDRESSES::add);
        }
    }

    public static boolean isEnabled() {
        return ADDRESSES.stream().allMatch(address -> NetworkUtils.isNetworkAvailable(address, 500));
    }
}
