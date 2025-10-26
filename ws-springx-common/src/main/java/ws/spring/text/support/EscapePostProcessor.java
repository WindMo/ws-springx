/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.text.support;

import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import ws.spring.aop.support.MethodCachedDynamicMethodMatcherPointcut;
import ws.spring.text.annotaion.Escape;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author WindShadow
 * @version 2023-05-01.
 */

public class EscapePostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

        Pointcut pointcut = new EscapeMethodMatcherPointcut();
        this.advisor = new DefaultPointcutAdvisor(pointcut, new EscapeMethodInterceptor());
    }

    /**
     * 接口{@link MethodMatcher}是动态切点/
     */
    private static class EscapeMethodMatcherPointcut extends MethodCachedDynamicMethodMatcherPointcut {

        @Override
        protected boolean doMatchMethod(Method method, Class<?> targetClass) {

            return Stream.of(method.getParameters())
                    .anyMatch(parameter -> AnnotatedElementUtils.hasAnnotation(parameter, Escape.class));
        }
    }
}
