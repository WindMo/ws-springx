/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

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
 * @author WindShadow
 * @version 2022-09-18.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {})
@Pattern(regexp = RegexpConstants.REGEXP_IP_V4)
@ReportAsSingleViolation
public @interface IPv4 {

    String message() default "{ws.spring.validate.annotation.IPv4.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
