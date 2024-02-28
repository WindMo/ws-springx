package ws.spring.restrict.annotation;

import org.springframework.core.annotation.AliasFor;
import ws.spring.restrict.RestrictedCriticalException;

import java.lang.annotation.*;

/**
 * 声明式频控注解，用于控制方法的调用频率。超过调用频率将以{@linkplain RestrictedCriticalException 临界异常}通知
 *
 * @author WindShadow
 * @version 2023-10-18.
 * @see ws.spring.restrict.FrequencyRestrictor
 * @see ws.spring.restrict.FrequencyRestrictService
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FrequencyRestrict {

    /**
     * 频控器名称（全局唯一）
     */
    String name() default "";

    @AliasFor("frequency")
    int value() default 1;

    /**
     * 频控依据spel表达式，表达式结果不能为null
     */
    String refer() default "";

    /**
     * 限定的频次，正整数
     */
    int frequency() default 1;

    /**
     * 持续时长（单位：秒），默认1秒，最低为1秒
     */
    long duration() default 1L;
}
