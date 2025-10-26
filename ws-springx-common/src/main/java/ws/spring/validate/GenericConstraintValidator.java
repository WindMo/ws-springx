/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.validate;

import org.springframework.lang.NonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * 通用的约束校验器，当被校验的值为null时，则不进行校验，即视为校验通过，这符合javax校验体系的约定，
 * 如果想约束null值，你应该使用{@link javax.validation.constraints.NotNull}注解，而不是在校验器身上做是否允许为null之类的设计，
 * 每个校验器只专注其单一的校验工作
 *
 * @author WindShadow
 * @version 2022-09-18.
 */

public abstract class GenericConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }
        return isValidValue(value, context);
    }

    protected abstract boolean isValidValue(@NonNull T value, ConstraintValidatorContext context);
}
