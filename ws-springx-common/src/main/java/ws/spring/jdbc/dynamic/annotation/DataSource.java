package ws.spring.jdbc.dynamic.annotation;

import java.lang.annotation.*;

/**
 * 用于指定bean的方法运行时所用使用的数据源，优先使用方法级注解声明的数据源，其次是类级别声明的。
 * 你必须将注解作用于最终使用的bean上，即继承过来的方法或类上的注解是无效的。
 *
 * @author WindShadow
 * @version 2024-04-02.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    /** 使用的数据源的名称，未指定则使用默认数据源 */
    String value() default "";
}
