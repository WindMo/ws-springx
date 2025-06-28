package ws.spring.text.annotaion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ws.spring.context.annotation.ExactConfiguration;
import ws.spring.text.support.EscapePostProcessor;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */

@ExactConfiguration
class EscapeConfiguration {

    @Bean
    public static EscapePostProcessor escapePostProcessor(@Autowired Environment environment) {

        EscapePostProcessor escapePostProcessor = new EscapePostProcessor();
        boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
        escapePostProcessor.setProxyTargetClass(proxyTargetClass);
        return escapePostProcessor;
    }
}
