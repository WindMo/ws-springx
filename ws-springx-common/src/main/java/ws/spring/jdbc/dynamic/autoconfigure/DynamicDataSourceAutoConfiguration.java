package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ws.spring.jdbc.dynamic.DataSourceSelector;
import ws.spring.jdbc.dynamic.DynamicDataSource;
import ws.spring.jdbc.dynamic.autoconfigure.sql.DynamicDataSourceInitializationConfiguration;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.ext.dynamic-datasource", name = "enabled", havingValue = "true")
@ConditionalOnClass(DataSource.class)
@AutoConfiguration(before = DataSourceAutoConfiguration.class)
public class DynamicDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(parameterizedContainer = DataSourceFactory.class, value = DataSourceProperties.class)
    public static DataSourceFactory<DataSourceProperties> defaultDataSourceFactory() {
        return new GenericDataSourceFactory();
    }

    @Configuration(proxyBeanMethods = false)
    @Import({DataSourceSwitchConfiguration.class, DynamicDataSourceInitializationConfiguration.class})
    @EnableConfigurationProperties(DynamicDataSourceProperties.class)
    static class DynamicDataSourceConfiguration {

        @Bean
        public static DynamicDataSource dynamicDataSource(DynamicDataSourceProperties properties,
                                                          DataSourceSelector selector,
                                                          DataSourceFactory<DataSourceProperties> factory) {

            DynamicDataSource dynamicDataSource = new DynamicDataSource(selector);
            Optional.ofNullable(properties.getDefaultDataSource())
                    .ifPresent(dataSource -> dynamicDataSource.setDefaultDataSource(factory.buildDataSource(dataSource)));
            Optional.ofNullable(properties.getDataSources())
                    .ifPresent(dataSources -> dataSources.forEach(ds -> dynamicDataSource.putDataSource(ds.getName(), factory.buildDataSource(ds))));
            return dynamicDataSource;
        }
    }
}
