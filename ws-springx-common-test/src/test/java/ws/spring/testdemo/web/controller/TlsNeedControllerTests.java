package ws.spring.testdemo.web.controller;

import org.springframework.boot.web.server.Ssl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import ws.spring.web.client.HttpClientAssistants;

/**
 * @author WindShadow
 * @version 2022-10-02.
 */
@ActiveProfiles({"ssl_need"})
public class TlsNeedControllerTests extends BaseTlsControllerTests {

    @Override
    protected void doTlsTest(boolean tls, boolean need, Ssl ssl) {

        RestTemplate restTemplate = HttpClientAssistants.newMutualAuthRestTemplate(
                ssl.getKeyStore(), ssl.getKeyStorePassword().toCharArray(), ssl.getKeyPassword().toCharArray(),
                ssl.getTrustStore(), ssl.getTrustStorePassword().toCharArray());
        doHttpTest(tls, true, restTemplate);
    }
}
