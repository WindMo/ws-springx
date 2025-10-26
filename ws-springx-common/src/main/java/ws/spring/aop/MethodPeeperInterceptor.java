/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import ws.spring.aop.annotation.ExposurePoint;

import java.lang.reflect.Method;

/**
 * @author WindShadow
 * @version 2022-01-21.
 */

public class MethodPeeperInterceptor implements MethodInterceptor {

    private final MethodPeeper<?> methodPeeper;

    public MethodPeeperInterceptor(MethodPeeper<?> methodPeeper) {

        Assert.notNull(methodPeeper, "The MethodArgumentsPeeper must not be null");
        this.methodPeeper = methodPeeper;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        ExposurePoint exposurePoint = AnnotationUtils.findAnnotation(method, ExposurePoint.class);
        if (exposurePoint != null) {

            Class<?> targetClass = invocation.getThis().getClass();
            Object[] arguments = invocation.getArguments();
            ReturnValuePeeper returnValueProcessor = methodPeeper.peekArguments(exposurePoint, targetClass, method, arguments);
            Object returnValue = null;
            Exception ex = null;
            try {
                returnValue = invocation.proceed();
                return returnValue;
            } catch (Exception thr) {
                ex = thr;
                throw thr;
            } finally {
                if (returnValueProcessor != null) {
                    returnValueProcessor.peekReturnValue(returnValue, ex);
                }
            }
        } else {
            return invocation.proceed();
        }
    }
}
