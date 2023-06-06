package ws.spring.testdemo.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ws.spring.testdemo.SpringExtendApplication;
import ws.spring.testdemo.SpringExtendApplicationTests;
import ws.spring.web.client.HttpClientAssistants;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WindShadow
 * @version 2022-10-02.
 */

public abstract class BaseTlsControllerTests extends SpringExtendApplicationTests {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private Environment env;

    @Test
    void baseTlsTest() {

        String[] profiles = env.getActiveProfiles();
        String arg = Stream.of(profiles).collect(Collectors.joining(",", "--spring.profiles.active=", ""));
        ConfigurableApplicationContext context = SpringApplication.run(SpringExtendApplication.class, arg);

        Ssl ssl = serverProperties.getSsl();
        boolean tls = ssl != null && ssl.isEnabled();
        boolean need = tls && Ssl.ClientAuth.NEED == ssl.getClientAuth();
        doHttpTest(ssl != null && ssl.isEnabled(), true, HttpClientAssistants.newRestTemplateBySsl(ssl));
        doTlsTest(tls, need, ssl);

        context.stop();
    }

    protected abstract void doTlsTest(boolean tls, boolean need, Ssl ssl);

    public void doHttpTest(boolean tls, boolean success, RestTemplate rest) {

        String url = (tls ? "https" : "http") + "://127.0.0.1:" + serverProperties.getPort() + "/ssl";
        if (success) {

            ResponseEntity<String> entity = rest.getForEntity(url, String.class);
            Assertions.assertSame(HttpStatus.OK, entity.getStatusCode());
        } else {
            Assertions.assertThrows(Exception.class, () -> rest.getForEntity(url, String.class));
        }
    }
}
