/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop;

import org.springframework.lang.Nullable;

/**
 * 返回值观测者，实现此接口用于观测{@linkplain ws.spring.aop.annotation.ExposurePoint 暴露点}方法的返回值
 *
 * @author WindShadow
 * @version 2022-01-23.
 * @see ws.spring.aop.annotation.ExposurePoint
 * @see MethodPeeper
 */

@FunctionalInterface
public interface ReturnValuePeeper<T> {

    /**
     * 观测暴露点方法的返回值
     *
     * @param returnValue 目标方法执行成功的返回值（void 的返回值为null）
     * @param ex          目标方法执行期间抛出的异常，无异常时其值为 null
     */
    void peekReturnValue(@Nullable T returnValue, @Nullable Exception ex);
}
