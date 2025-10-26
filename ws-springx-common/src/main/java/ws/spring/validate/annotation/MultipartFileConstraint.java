/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.validate.annotation;

import ws.spring.validate.MultipartFileConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * @author WindShadow
 * @version 2022-11-22.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {MultipartFileConstraintValidator.class})
@Documented
public @interface MultipartFileConstraint {

    long UNLIMITED_SIZE = -1L;

    String[] contentTypes() default {};

    boolean allowNullContentType() default false;

    long maxSize() default UNLIMITED_SIZE;

    String message() default "{ws.spring.validate.annotation.MultipartFileConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
