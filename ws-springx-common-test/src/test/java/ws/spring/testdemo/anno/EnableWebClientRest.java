package ws.spring.testdemo.anno;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;
import org.springframework.boot.test.autoconfigure.web.client.WebClientRestTemplateAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2025-07-26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration(WebClientRestTemplateAutoConfiguration.class)
@PropertyMapping("spring.test.webclient")
public @interface EnableWebClientRest {
    String registerRestTemplate() default "";
}
