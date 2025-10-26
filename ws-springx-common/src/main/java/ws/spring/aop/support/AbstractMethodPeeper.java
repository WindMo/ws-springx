/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import ws.spring.aop.MethodPeeper;
import ws.spring.aop.ReturnValuePeeper;
import ws.spring.aop.annotation.ExposurePoint;
import ws.spring.aop.exception.PeekMethodDeclarationException;

import java.lang.reflect.Method;

/**
 * @author WindShadow
 * @version 2022-01-22.
 */

public abstract class AbstractMethodPeeper<T> implements MethodPeeper<T>, InitializingBean {

    private GlobalMethodPeekHandler globalMethodPeekHandler;

    public AbstractMethodPeeper() {
    }

    public AbstractMethodPeeper(GlobalMethodPeekHandler globalMethodPeekHandler) {
        this.globalMethodPeekHandler = globalMethodPeekHandler;
    }

    public GlobalMethodPeekHandler getGlobalLogAdvice() {
        return globalMethodPeekHandler;
    }

    public void setGlobalLogAdvice(GlobalMethodPeekHandler globalMethodPeekHandler) {
        this.globalMethodPeekHandler = globalMethodPeekHandler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.state(this.globalMethodPeekHandler != null, "The globalMethodPeekHandler is not set");
    }

    @Override
    public ReturnValuePeeper<T> peekArguments(ExposurePoint exposurePoint, Class<?> clazz, Method exposurePointMethod, Object... args) {

        ReturnValuePeeper<T> returnValuePeeper = null;
        MethodPeeper<T> methodPeeper = findMethodPeeper(exposurePoint, clazz, exposurePointMethod);
        if (methodPeeper == null) {
            this.globalMethodPeekHandler.handleMissMethodPeeper(exposurePoint, exposurePointMethod, ClassUtils.getUserClass(exposurePointMethod.getDeclaringClass()));
        } else {

            methodPeeper = enhanceExceptionNotifyMethodPeeper(methodPeeper);
            returnValuePeeper = methodPeeper.peekArguments(exposurePoint, clazz, exposurePointMethod, args);
        }
        return returnValuePeeper;
    }

    @Nullable
    protected abstract MethodPeeper<T> findMethodPeeper(ExposurePoint exposurePoint, Class<?> clazz, Method exposurePointMethod);

    protected final MethodPeeper<T> enhanceExceptionNotifyMethodPeeper(@NonNull MethodPeeper<T> methodPeeper) {

        Assert.notNull(methodPeeper, "The MethodPeeper is must not be null");
        return (exposurePoint, clazz, method, args) -> {

            ReturnValuePeeper<T> returnValuePeeper = invokeMethodPeeper(methodPeeper, exposurePoint, clazz, method, args);
            if (returnValuePeeper != null) {

                return (returnValue, ex) -> {
                    try {
                        returnValuePeeper.peekReturnValue(returnValue, ex);
                    } catch (RuntimeException exWhenPeekReturnValue) {
                        // 捕获观测暴露点方法的返回值时发生的异常，进行全局处理
                        this.globalMethodPeekHandler.handlePeekReturnException(exWhenPeekReturnValue, returnValue);
                    }
                };
            }
            return null;
        };
    }

    @Nullable
    private ReturnValuePeeper<T> invokeMethodPeeper(@NonNull MethodPeeper<T> methodPeeper, ExposurePoint exposurePoint, Class<?> clazz, Method exposurePointMethod, Object... args) {

        Assert.notNull(methodPeeper, "The MethodPeeper must not be null");
        ReturnValuePeeper<T> returnValuePeeper = null;
        try {
            returnValuePeeper = methodPeeper.peekArguments(exposurePoint, clazz, exposurePointMethod, args);
        } catch (PeekMethodDeclarationException pmde) {
            throw pmde;
        } catch (RuntimeException exception) {
            // 捕获观测暴露点方法的参数值时发生的异常，进行全局处理
            this.globalMethodPeekHandler.handlePeekArgumentsException(exception, args);
        }
        return returnValuePeeper;
    }
}
