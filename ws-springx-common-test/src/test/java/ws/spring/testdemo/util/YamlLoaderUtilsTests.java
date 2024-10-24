package ws.spring.testdemo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;
import ws.spring.util.YamlLoaderUtils;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

/**
 * @author WindShadow
 * @version 2024-10-24.
 */
public class YamlLoaderUtilsTests {

    private static final String YAML_LOCATION = "classpath:application.yml";

    private Resource yamlResource;

    @BeforeEach
    void loadYamlResource() throws FileNotFoundException {

        yamlResource = new UrlResource(ResourceUtils.getURL(YAML_LOCATION));
    }

    @Test
    void yamlToPropertiesTest() {

        Properties properties = YamlLoaderUtils.yamlToProperties(yamlResource);
        Assertions.assertNotNull(properties);
    }

    @Test
    void yamlToMap() {

        Map<String, Object> map = YamlLoaderUtils.yamlToMap(yamlResource);
        Assertions.assertNotNull(map);
    }
}
