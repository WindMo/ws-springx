/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 通过此注解你可以将某个Spring bean 的方法暴露出去，
 * 使{@linkplain ws.spring.aop.MethodPeeper 方法观测者}们有可能观测该方法的执行过程
 *
 * @author WindShadow
 * @version 2022-01-21.
 * @see ws.spring.aop.MethodPeeper
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExposurePoint {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
