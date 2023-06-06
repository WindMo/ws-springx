package ws.mybatis.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author WindShadow
 * @version 2022-01-12.
 */

@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Padding {

    /**
     * {@code Controller}控制器方法返回值类型为指定类型时，将实际分页结果数据追加到返回值中
     *
     * @return 目标类型
     */
    Class<?> type() default Object.class;

    /**
     * 设置追加实际分页的页码数据到目标类型的指定属性
     *
     * @return 目标类型保存页码的属性名
     */
    String pageNumberProperty() default "";

    /**
     * 设置追加实际分页的页大小数据到目标类型的指定属性
     *
     * @return 目标类型保存页大小的属性名
     */
    String pageSizeProperty() default "";

    /**
     * 设置追加实际分页的总数到目标类型的指定属性
     *
     * @return 目标类型保存总数的属性名
     */
    String totalProperty() default "";
}
