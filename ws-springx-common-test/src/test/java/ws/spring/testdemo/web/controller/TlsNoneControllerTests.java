package ws.spring.testdemo.web.controller;

import org.springframework.boot.web.server.Ssl;
import org.springframework.test.context.ActiveProfiles;
import ws.spring.web.client.HttpClientAssistants;

/**
 * @author WindShadow
 * @version 2022-10-02.
 */
@ActiveProfiles({"ssl_none"})
public class TlsNoneControllerTests extends BaseTlsControllerTests {

    @Override
    protected void doTlsTest(boolean tls, boolean need, Ssl ssl) {

        doHttpTest(tls, true, HttpClientAssistants.newUnidirectionalAuthRestTemplate(ssl.getTrustStore(), ssl.getTrustStorePassword().toCharArray()));
        doHttpTest(tls, true, HttpClientAssistants.newIgnoreAuthServerRestTemplate(ssl.getKeyStore(), ssl.getKeyStorePassword().toCharArray(), ssl.getKeyPassword().toCharArray()));
        doHttpTest(tls, true, HttpClientAssistants.newIgnoreAuthServerRestTemplate());
    }
}
