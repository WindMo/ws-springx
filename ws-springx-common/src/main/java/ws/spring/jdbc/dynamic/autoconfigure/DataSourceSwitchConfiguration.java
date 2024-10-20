package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;
import ws.spring.jdbc.dynamic.annotation.DataSourceSwitchPostProcessor;

@Configuration(proxyBeanMethods = false)
@Import(DataSourceSelectorConfiguration.class)
class DataSourceSwitchConfiguration {

    @Bean
    public static DataSourceSwitchPostProcessor dataSourceSwitchPostProcessor(SwitchableDataSourceSelector selector, Environment env) {

        DataSourceSwitchPostProcessor processor = new DataSourceSwitchPostProcessor(selector);
        boolean proxyTargetClass = env.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
        processor.setProxyTargetClass(proxyTargetClass);
        return processor;
    }
}