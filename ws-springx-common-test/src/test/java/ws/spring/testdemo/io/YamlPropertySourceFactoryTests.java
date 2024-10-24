package ws.spring.testdemo.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.ResourceUtils;
import ws.spring.io.support.YamlPropertySourceFactory;

import java.io.IOException;

/**
 * @author WindShadow
 * @version 2024-10-24.
 */
public class YamlPropertySourceFactoryTests {

    private static final String YAML_LOCATION = "classpath:application.yml";

    @Test
    void createPropertySourceTest() throws IOException {

        YamlPropertySourceFactory factory = new YamlPropertySourceFactory();
        Resource resource = new UrlResource(ResourceUtils.getURL(YAML_LOCATION));
        Assertions.assertNotNull(factory.createPropertySource(null, new EncodedResource(resource)));
        Assertions.assertNotNull(factory.createPropertySource("", new EncodedResource(resource)));
        Assertions.assertNotNull(factory.createPropertySource("unused", new EncodedResource(resource)));
    }
}
