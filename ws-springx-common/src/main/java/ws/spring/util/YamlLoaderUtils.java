package ws.spring.util;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @author WindShadow
 * @version 2024-10-24.
 */
public class YamlLoaderUtils {

    public static Properties yamlToProperties(Resource... yamlResources) {
        return parseResources(YamlPropertiesFactoryBean::new, yamlResources);
    }

    public static Map<String, Object> yamlToMap(Resource... yamlResources) {
        return parseResources(YamlMapFactoryBean::new, yamlResources);
    }

    @NonNull
    private static <T, F extends FactoryBean<T>> T parseResources(Supplier<F> supplier, Resource... yamlResources) {

        F factory = supplier.get();
        Assert.isInstanceOf(YamlProcessor.class, factory);
        YamlProcessor processor = (YamlProcessor) factory;
        processor.setResources(yamlResources);
        try {

            T result = factory.getObject();
            Assert.state(result != null, "Parse yaml resources failed");
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
