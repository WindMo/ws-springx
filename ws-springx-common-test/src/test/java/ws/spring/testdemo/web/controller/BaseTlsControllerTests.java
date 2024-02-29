package ws.spring.testdemo.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.server.Ssl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import ws.spring.testdemo.SpringxAppWebTests;
import ws.spring.web.client.HttpClientAssistants;

/**
 * @author WindShadow
 * @version 2022-10-02.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTlsControllerTests extends SpringxAppWebTests {

    @Autowired
    private ServerProperties serverProperties;

    @LocalServerPort
    private int localServerPort;

    @Test
    void baseTlsTest() {

        Ssl ssl = serverProperties.getSsl();
        boolean tls = ssl != null && ssl.isEnabled();
        boolean need = tls && Ssl.ClientAuth.NEED == ssl.getClientAuth();
        doHttpTest(ssl != null && ssl.isEnabled(), true, new RestTemplate(buildHttpRequestFactory(ssl)));
        doTlsTest(tls, need, ssl);
    }

    protected abstract void doTlsTest(boolean tls, boolean need, Ssl ssl);

    public void doHttpTest(boolean tls, boolean success, RestTemplate rest) {

        String url = (tls ? "https" : "http") + "://127.0.0.1:" + localServerPort + "/ssl";
        if (success) {

            ResponseEntity<String> entity = rest.getForEntity(url, String.class);
            Assertions.assertSame(HttpStatus.OK, entity.getStatusCode());
        } else {
            Assertions.assertThrows(Exception.class, () -> rest.getForEntity(url, String.class));
        }
    }

    public static HttpComponentsClientHttpRequestFactory buildHttpRequestFactory(@Nullable Ssl ssl) {

        if (ssl != null) {

            String keyStore = ssl.getKeyStore();
            String keyStorePassword = ssl.getKeyStorePassword();
            String keyPassword = ssl.getKeyPassword();
            if (keyStore != null && keyStorePassword != null && keyPassword != null) {

                Ssl.ClientAuth clientAuth = ssl.getClientAuth();
                if (Ssl.ClientAuth.NEED == clientAuth) {

                    String trustStore = ssl.getTrustStore();
                    Assert.notNull(trustStore, "The trustStore must not be null when client auth is required");
                    String trustStorePassword = ssl.getTrustStorePassword();
                    Assert.notNull(trustStorePassword, "The trustStorePassword must not be null when client auth is required");
                    return HttpClientAssistants.getMutualAuthClientHttpRequestFactory(keyStore, keyStorePassword.toCharArray(), keyPassword.toCharArray(),
                            trustStore, trustStorePassword.toCharArray());
                }
                return HttpClientAssistants.getIgnoreAuthServerClientHttpRequestFactory(keyStore, keyStorePassword.toCharArray(), keyPassword.toCharArray());
            }
        }
        return HttpClientAssistants.getIgnoreAuthServerClientHttpRequestFactory();
    }
}
