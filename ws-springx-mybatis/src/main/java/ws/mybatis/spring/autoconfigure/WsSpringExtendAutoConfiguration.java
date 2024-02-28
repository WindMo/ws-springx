package ws.mybatis.spring.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.mybatis.spring.page.WebMvcPageConfigurer;

/**
 * @author WindShadow
 * @version 2022-01-09.
 */
@Configuration(proxyBeanMethods = false)
public class WsSpringExtendAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public static WebMvcPageConfigurer webMvcPageConfigurer() {

        return new WebMvcPageConfigurer();
    }
}
