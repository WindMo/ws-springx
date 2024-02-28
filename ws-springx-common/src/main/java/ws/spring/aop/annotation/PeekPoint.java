package ws.spring.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2022-01-22.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PeekPoint {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean asyn() default false;
}
