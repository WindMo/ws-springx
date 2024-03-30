package ws.spring.testdemo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import ws.spring.util.SecurityKeyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author WindShadow
 * @version 2024-03-30.
 * @see SecurityKeyUtils
 */
public class SecurityKeyUtilsTests {

    private final String location = "classpath:key/rsa.private";
    private char[] password;

    @BeforeEach
    void before() throws Exception {

        UrlResource resource = new UrlResource(ResourceUtils.getURL("classpath:key/password.txt"));
        password = StreamUtils.copyToString(resource.getInputStream() , StandardCharsets.UTF_8).toCharArray();
    }

    @Test
    void signatureTest() throws IOException {

        PublicKey publicKey = SecurityKeyUtils.readPublicKey(location, password);
        PrivateKey privateKey = SecurityKeyUtils.readPrivateKey(location, password);
        Assertions.assertTrue(SecurityKeyUtils.matchKeys("RSA", publicKey, privateKey));
    }
}
