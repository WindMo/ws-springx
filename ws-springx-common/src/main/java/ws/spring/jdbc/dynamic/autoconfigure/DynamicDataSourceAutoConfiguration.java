package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ws.spring.jdbc.dynamic.DataSourceFactory;
import ws.spring.jdbc.dynamic.DataSourceSelector;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.ext.dynamic-datasource", name = "enabled", havingValue = "true")
@ConditionalOnClass(DataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DynamicDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(parameterizedContainer = DataSourceFactory.class, value = DataSourceProperties.class)
    public static DataSourceFactory<DataSourceProperties> defaultDataSourceFactory() {
        return new GenericDataSourceFactory();
    }

    @Configuration(proxyBeanMethods = false)
    @Import(DataSourceSwitchConfiguration.class)
    @EnableConfigurationProperties(DynamicDataSourceProperties.class)
    static class DynamicDataSourceConfiguration {

        @Bean
        public static DataSource dynamicDataSource(DynamicDataSourceProperties properties,
                                                   DataSourceSelector selector,
                                                   DataSourceFactory<DataSourceProperties> factory) {

            GenericDynamicDataSource dynamicDataSource = new GenericDynamicDataSource(selector, factory);
            dynamicDataSource.setDefaultTargetDataSource(properties.getDefaultDataSource());
            Optional.ofNullable(properties.fetchDataSourceMap())
                    .ifPresent(ds -> dynamicDataSource.setTargetDataSources(new HashMap<>(ds)));
            return dynamicDataSource;
        }
    }
}
