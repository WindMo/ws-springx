package ws.spring.aop.annotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import ws.spring.aop.autoconfigure.MethodPeekAutoConfigure;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2022-01-22.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration(MethodPeekAutoConfigure.class)
public @interface EnableMethodPeek {

}
