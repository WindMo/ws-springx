package ws.mybatis.spring.annotation;

import java.lang.annotation.*;

/**
 * 用于绑定页码参数的注解，见{@linkplain EnablePage EnablePage注解使用示例}
 *
 * @author WindShadow
 * @version 2021-12-26.
 * @see EnablePage
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageNumber {
}