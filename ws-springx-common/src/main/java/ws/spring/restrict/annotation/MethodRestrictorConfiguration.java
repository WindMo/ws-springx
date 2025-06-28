package ws.spring.restrict.annotation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ws.spring.context.annotation.ExactConfiguration;
import ws.spring.restrict.FrequencyRestrictRegistrar;
import ws.spring.restrict.FrequencyRestrictService;
import ws.spring.restrict.MethodRestrictorPostProcessor;
import ws.spring.restrict.support.SimpleFrequencyRestrictService;

/**
 * @author WindShadow
 * @version 2024-01-26.
 */

@ExactConfiguration
class MethodRestrictorConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public static FrequencyRestrictService frequencyRestrictService() {

        return new SimpleFrequencyRestrictService();
    }

    @Bean
    public static MethodRestrictorPostProcessor methodRestrictorPostProcessor(FrequencyRestrictRegistrar registrar, BeanFactory beanFactory, Environment environment) {

        MethodRestrictorPostProcessor processor = new MethodRestrictorPostProcessor(registrar, beanFactory);
        boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
        processor.setProxyTargetClass(proxyTargetClass);
        return processor;
    }
}
