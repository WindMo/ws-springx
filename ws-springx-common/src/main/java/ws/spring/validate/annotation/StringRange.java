package ws.spring.validate.annotation;

import ws.spring.validate.StringRangeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 字符串元素范围约束注解，使用方式如下（以Spring环境示例）：
 * <pre class="code">
 * &#064;Validated
 * &#064;Service
 * public class CustomService {
 *     public void aMethod(&#064;StringRange({"aaa","bbb"}) String str)
 *          // ...
 *     }
 * }
 * </pre>
 * 当参数<code>str</code>不为"aaa","bbb"之一时，则校验不通过，反之通过
 *
 * @author WindShadow
 * @version 2021-12-16.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {StringRangeConstraintValidator.class})
public @interface StringRange {

    /**
     * 字符串元素范围
     *
     * @return value 至少必须配置一个元素
     */
    String[] value();

    boolean trim() default false;

    boolean ignoreCase() default false;

    String message() default "{ws.spring.validate.annotation.StringRange.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
