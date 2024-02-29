package ws.spring.testdemo.web.controller;

import org.springframework.boot.web.server.Ssl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import ws.spring.testdemo.util.HttpClientAssistants;

/**
 * @author WindShadow
 * @version 2022-10-02.
 */
@ActiveProfiles({"ssl_none"})
public class TlsNoneControllerTests extends BaseTlsControllerTests {

    @Override
    protected void doTlsTest(boolean tls, boolean need, Ssl ssl, RestTemplate rest) {

        rest.setRequestFactory(HttpClientAssistants.getUnidirectionalAuthClientHttpRequestFactory(ssl.getTrustStore(), ssl.getTrustStorePassword().toCharArray()));
        doHttpTest(tls, true, rest);
        rest.setRequestFactory(HttpClientAssistants.getIgnoreAuthServerClientHttpRequestFactory(ssl.getKeyStore(), ssl.getKeyStorePassword().toCharArray(), ssl.getKeyPassword().toCharArray()));
        doHttpTest(tls, true, rest);
        rest.setRequestFactory(HttpClientAssistants.getIgnoreAuthServerClientHttpRequestFactory());
        doHttpTest(tls, true, rest);
    }
}
