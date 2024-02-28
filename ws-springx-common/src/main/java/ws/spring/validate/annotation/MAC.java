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

/**
 * @author WindShadow
 * @version 2022-09-18.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {})
@Pattern(regexp = RegexpConstants.REGEXP_MAC)
@ReportAsSingleViolation
public @interface MAC {

    String message() default "{ws.spring.validate.annotation.MAC.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
