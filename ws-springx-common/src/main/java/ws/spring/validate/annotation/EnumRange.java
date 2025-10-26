/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.validate.annotation;

import ws.spring.validate.EnumRangeRangeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.ConstraintDeclarationException;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 限制枚举范围的约束注解，使用方式如下（以Spring环境示例）：
 * <pre class="code">
 * public enum MyEnum {
 *     // 金木水火土
 *     JIN,MU,SHUI,HUO,TU
 * }
 * &#064;Validated
 * &#064;Service
 * public class CustomService {
 *     public void aMethod(&#064;EnumRange(enumType = MyEnum.class, enums = {"JIN","MU","SHUI"}) MyEnum param)
 *          // ...
 *     }
 * }
 * </pre>
 * 当枚举参数<code>param</code>不为"JIN","MU","SHUI"之一时，则校验不通过，反之通过
 * <p>当{@link EnumRange}修饰的参数类型和{@link EnumRange#enumType()}不一致时，将在校验时抛出{@linkplain ConstraintDeclarationException 约束声明异常}，开发者应该避免约束声明错误
 *
 * @author WindShadow
 * @version 2021-12-16.
 * @see EnumRangeRangeConstraintValidator
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {EnumRangeRangeConstraintValidator.class})
public @interface EnumRange {

    /**
     * 枚举类型
     */
    Class<? extends Enum<?>> enumType();

    /**
     * 枚举名称数组，其中的字符串必须是可以通过{@link Enum#valueOf(Class, String)}方法可以得到枚举实例的枚举名称
     *
     * @return enums 必须至少配置一个元素
     */
    String[] enums();

    String message() default "{ws.spring.validate.annotation.EnumRange.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
