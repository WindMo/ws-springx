package ws.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2022-01-22.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Peeper {

    /**
     * @return 观测点的Class
     */
    Class<?> value();
}
