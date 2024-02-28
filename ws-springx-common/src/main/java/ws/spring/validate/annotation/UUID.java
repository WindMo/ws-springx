package ws.spring.validate.annotation;

import ws.spring.constant.RegexpConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * 约束字符序列为UUID
 *
 * @author WindShadow
 * @version 2022-08-27.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {})
@Pattern(regexp = RegexpConstants.REGEXP_UUID)
@ReportAsSingleViolation
public @interface UUID {

    String message() default "{ws.spring.validate.annotation.UUID.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
