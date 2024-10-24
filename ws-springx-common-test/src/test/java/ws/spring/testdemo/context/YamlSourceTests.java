package ws.spring.testdemo.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.util.StringUtils;
import ws.spring.context.annotation.YamlSource;
import ws.spring.testdemo.SpringxAppTests;

/**
 * @author WindShadow
 * @version 2024-10-24.
 */
public class YamlSourceTests extends SpringxAppTests {

    @Value("${app.test.yaml-source.custom-value:}")
    private String customValue;

    @Test
    void loadYamlSourceTest() {
        Assertions.assertTrue(StringUtils.hasText(customValue));
    }

    @YamlSource("classpath:custom-config.yml")
    @TestConfiguration
    static class BaseConfig {}
}
