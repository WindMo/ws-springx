package ws.spring.context.annotation;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author WindShadow
 * @version 2025-06-23.
 */
class TypeExcludeFilterRegistrar implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    @Override
    public void initialize(ConfigurableApplicationContext context) {

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton(ModuleConfigurationTypeExcludeFilter.class.getName(), new ModuleConfigurationTypeExcludeFilter());
    }
}
