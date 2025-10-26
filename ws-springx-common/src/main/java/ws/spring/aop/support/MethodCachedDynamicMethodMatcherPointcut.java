/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop.support;

import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */

public abstract class MethodCachedDynamicMethodMatcherPointcut extends DynamicMethodMatcherPointcut {

    private final Map<Method, Object> methodCache = new ConcurrentReferenceHashMap<>(128);

    @Override
    public final boolean matches(Method method, Class<?> targetClass) {

        if (methodCache.containsKey(method)) return true;
        boolean match = doMatchMethod(method, targetClass);
        if (match) {
            methodCache.put(method, this);
        }
        return match;
    }

    @Override
    public final boolean matches(Method method, Class<?> targetClass, Object... args) {
        return matches(method, targetClass);
    }

    protected abstract boolean doMatchMethod(Method method, Class<?> targetClass);
}
