package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;
import ws.spring.jdbc.dynamic.support.ThreadLocalDataSourceSelector;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DataSourceSelectorProperties.class)
@ConditionalOnMissingBean(SwitchableDataSourceSelector.class)
class DataSourceSelectorConfiguration {

    @Bean(destroyMethod = "resetSelected")
    public static SwitchableDataSourceSelector dataSourceSelector(DataSourceSelectorProperties properties) {
        return new ThreadLocalDataSourceSelector(properties.isSelectorInheritable());
    }
}