package ws.spring.io.support;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.StringUtils;
import ws.spring.util.YamlLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author WindShadow
 * @version 2024-10-24.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

        Resource targetResource = resource.getResource();
        Properties properties = YamlLoaderUtils.yamlToProperties(targetResource);
        String nameToUse = name;
        if (!StringUtils.hasText(nameToUse)) {
            nameToUse = getNameForResource(targetResource);
        }
        return new PropertiesPropertySource(nameToUse, properties);
    }

    protected static String getNameForResource(Resource resource) {

        String name = resource.getDescription();
        if (!StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }
}
