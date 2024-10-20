package ws.spring.jdbc.dynamic.autoconfigure.sql;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.spring.jdbc.dynamic.DynamicDataSource;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DynamicDataSourceSqlInitProperties.class)
public class DynamicDataSourceInitializationConfiguration {

    @Bean
    public static DynamicDataSourceScriptInitializer dynamicDataSourceScriptInitializer(DynamicDataSource dataSource, DynamicDataSourceSqlInitProperties properties) {
        return new DynamicDataSourceScriptInitializer(dataSource, properties);
    }
}
