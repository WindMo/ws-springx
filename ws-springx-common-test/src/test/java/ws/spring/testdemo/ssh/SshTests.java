package ws.spring.testdemo.ssh;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import ws.spring.constant.NetworkConstants;
import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshOperator;
import ws.spring.ssh.SshService;
import ws.spring.testdemo.SpringxAppWebTests;
import ws.spring.testdemo.anno.EnableWebClientRest;
import ws.spring.testdemo.util.HttpClientAssistants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * forward: 127.0.0.1:{localPort} -> [ssh source] -> {localServer}:{serverPort}
 *
 * @author WindShadow
 * @version 2024-03-02.
 */
@Slf4j
@EnabledIf(expression = "#{T(ws.spring.testdemo.ssh.SshTestCondition).isEnabled()}",
        reason = "The network connection in this ssh environment is abnormal"
)
@ActiveProfiles("ssh")
@EnableWebClientRest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SshTests extends SpringxAppWebTests {

    private final Random random = new Random();

    @Autowired
    private RestTemplate rest;

    @Value("${spring.ext.ssh.test.local-server}")
    private String localServer;

    @Value("${spring.ext.ssh.test.local-port}")
    private int localPort;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private SshService sshService;
    private SshOperator operator;

    public static final String MAIN_SSH_SOURCE = "main";
    public static final String MAIN_KEY_SSH_SOURCE = "main-key";

    private static String useSshSource = MAIN_SSH_SOURCE;

    @BeforeEach
    void initSshOperator() {

        if (MAIN_KEY_SSH_SOURCE.equals(useSshSource)) {
            useSshSource = MAIN_SSH_SOURCE;
        } else {
            useSshSource = MAIN_KEY_SSH_SOURCE;
        }
        operator = sshService.buildSshOperator(useSshSource);
    }

    @RepeatedTest(2)
    void sshExecTest() {

        int num = random.nextInt(100);
        String command = "echo " + num;
        String outStr = operator.exec(command);
        log.info("outStr: {}", outStr);
        Assertions.assertEquals(num + "\n", outStr);
    }

    @RepeatedTest(2)
    void shellStreamTest() {

        String command = "echo " + random.nextInt(100);
        String exit = "exit";
        ByteArrayInputStream in = new ByteArrayInputStream((command + "\n" + exit + "\n").getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        operator.shell(in, out);
        String outText = StreamUtils.copyToString(out, StandardCharsets.UTF_8);
        Assertions.assertTrue(outText.contains(command));
        Assertions.assertTrue(outText.contains(exit));
        Assertions.assertTrue(outText.contains("logout"));
    }

    @RepeatedTest(2)
    void shellInvertedStreamTest() {

        String command = "echo " + random.nextInt(100);
        String exit = "exit";
        operator.shell(in -> {
                    try {
                        StreamUtils.copy(command + "\n", StandardCharsets.UTF_8, in);
                        StreamUtils.copy(exit + "\n", StandardCharsets.UTF_8, in);
                        in.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                out -> {
                    try {
                        String outText = StreamUtils.copyToString(out, StandardCharsets.UTF_8);
                        Assertions.assertTrue(outText.contains(command));
                        Assertions.assertTrue(outText.contains(exit));
                        Assertions.assertTrue(outText.contains("logout"));
                        out.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @RepeatedTest(2)
    void shellInvertedInputStreamTest() {

        String command = "echo " + random.nextInt(100);
        String exit = "exit";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        operator.shell(in -> {
                    try {
                        StreamUtils.copy(command + "\n", StandardCharsets.UTF_8, in);
                        StreamUtils.copy(exit + "\n", StandardCharsets.UTF_8, in);
                        in.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, out);
        String outText = StreamUtils.copyToString(out, StandardCharsets.UTF_8);
        Assertions.assertTrue(outText.contains(command));
        Assertions.assertTrue(outText.contains(exit));
        Assertions.assertTrue(outText.contains("logout"));
    }

    @RepeatedTest(2)
    void shellInvertedOutputStreamTest() {

        String command = "echo " + random.nextInt(100);
        String exit = "exit";
        ByteArrayInputStream in = new ByteArrayInputStream((command + "\n" + exit + "\n").getBytes(StandardCharsets.UTF_8));
        operator.shell(in,
                out -> {
                    try {
                        String outText = StreamUtils.copyToString(out, StandardCharsets.UTF_8);
                        Assertions.assertTrue(outText.contains(command));
                        Assertions.assertTrue(outText.contains(exit));
                        Assertions.assertTrue(outText.contains("logout"));
                        out.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @RepeatedTest(2)
    void forwardTest() throws Exception {

        PortForwardingTracker holder = operator.forward(localServer, serverPort);
        forwardTest(holder.getLocalPort());
        holder.close();
    }

    @RepeatedTest(2)
    void autoForwardTest() {

        forwardTest(localPort);
    }

    private void forwardTest(int port) {

        String url = String.format("http://%s:%d/ssh", NetworkConstants.LOCALHOST_IP_V4, port);
        rest.setRequestFactory(HttpClientAssistants.getIgnoreAuthServerClientHttpRequestFactory());
        ResponseEntity<String> entity = rest.getForEntity(url, String.class);
        Assertions.assertSame(HttpStatus.OK, entity.getStatusCode());
    }
}
