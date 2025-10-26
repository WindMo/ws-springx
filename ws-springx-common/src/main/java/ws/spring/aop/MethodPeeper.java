/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop;

import org.springframework.lang.Nullable;
import ws.spring.aop.annotation.ExposurePoint;

import java.lang.reflect.Method;

/**
 * 实现此接口用于观测{@linkplain ExposurePoint 暴露点}方法执行过程
 *
 * @author WindShadow
 * @version 2022-01-21.
 * @see ExposurePoint
 * @see ReturnValuePeeper
 */

@FunctionalInterface
public interface MethodPeeper<T> {

    /**
     * 观测暴露点方法的执行过程，先观测暴露点方法的参数值，
     * 通过返回一个{@linkplain ReturnValuePeeper 返回值观测者}观测暴露点方法的返回值
     *
     * @param exposurePoint
     * @param method        暴露点方法
     * @param args          暴露点方法参数值
     * @return 返回值观测者
     */
    @Nullable
    ReturnValuePeeper<T> peekArguments(ExposurePoint exposurePoint, Class<?> clazz, Method method, Object... args);
}
