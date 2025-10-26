/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import ws.spring.util.SecurityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author WindShadow
 * @version 2024-03-30.
 * @see SecurityUtils
 */
public class SecurityUtilsTests {

    private final String location = "classpath:key/rsa.private";
    private char[] password;

    @BeforeEach
    void before() throws Exception {

        UrlResource resource = new UrlResource(ResourceUtils.getURL("classpath:key/password.txt"));
        password = StreamUtils.copyToString(resource.getInputStream() , StandardCharsets.UTF_8).toCharArray();
    }

    @Test
    void signatureTest() throws IOException {

        PublicKey publicKey = SecurityUtils.readPublicKey(location, password);
        PrivateKey privateKey = SecurityUtils.readPrivateKey(location, password);
        Assertions.assertTrue(SecurityUtils.matchKeys("RSA", publicKey, privateKey));
    }
}
