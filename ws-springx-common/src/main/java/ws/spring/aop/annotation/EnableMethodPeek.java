package ws.spring.aop.annotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2022-01-22.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration(MethodPeekConfiguration.class)
public @interface EnableMethodPeek {

}
