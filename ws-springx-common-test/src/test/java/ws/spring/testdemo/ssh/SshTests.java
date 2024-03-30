package ws.spring.testdemo.ssh;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import ws.spring.constant.NetworkConstants;
import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshOperator;
import ws.spring.ssh.SshService;
import ws.spring.testdemo.SpringxAppWebTests;
import ws.spring.web.client.HttpClientAssistants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author WindShadow
 * @version 2024-03-02.
 */
@Slf4j
@ActiveProfiles("ssh")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SshTests extends SpringxAppWebTests {

    private final Random random = new Random();

    @Value("${spring.ext.ssh.test.local-server}")
    private String localServer;

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private SshService sshService;
    private SshOperator operator;

    @BeforeEach
    void initSshOperator() {

        String mainSsh = "main-key";
        operator = sshService.buildSshOperator(mainSsh);
    }

    @Test
    void sshExecTest() {

        int num = random.nextInt(100);
        String command = "echo " + num;
        String outStr = operator.exec(command);
        log.info("outStr: {}", outStr);
        Assertions.assertEquals(num + "\n", outStr);
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
    void forwardTest() throws Exception {

        PortForwardingTracker holder = operator.forward(localServer, localServerPort);

        String url = String.format("http://%s:%d/ssh", NetworkConstants.LOCALHOST_IP_V4, holder.getLocalPort());
        RestTemplate rest = HttpClientAssistants.newIgnoreAuthServerRestTemplate();
        ResponseEntity<String> entity = rest.getForEntity(url, String.class);
        Assertions.assertSame(HttpStatus.OK, entity.getStatusCode());

        holder.close();
    }
}
