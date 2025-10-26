/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import ws.spring.restrict.annotation.FrequencyRestrict;

/**
 * @author WindShadow
 * @version 2024-01-26.
 */

public class MethodRestrictorPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor implements InitializingBean {

    private final FrequencyRestrictRegistrar registrar;
    private final BeanFactory beanFactory;

    public MethodRestrictorPostProcessor(FrequencyRestrictRegistrar registrar, BeanFactory beanFactory) {
        this.registrar = registrar;
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Pointcut pointcut = new AnnotationMatchingPointcut(null, FrequencyRestrict.class, true);
        this.advisor = new DefaultPointcutAdvisor(pointcut, new MethodFrequencyRestrictorInterceptor(this.registrar, beanFactory, new DefaultParameterNameDiscoverer(), new SpelExpressionParser()));
    }
}
