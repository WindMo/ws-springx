/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import ws.spring.aop.annotation.ExposurePoint;

/**
 * @author WindShadow
 * @version 2022-01-21.
 */

public class MethodPeeperPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor implements InitializingBean {

    private final MethodPeeper<?> methodPeeper;

    public MethodPeeperPostProcessor(MethodPeeper<?> methodPeeper) {

        Assert.notNull(methodPeeper, "The MethodAdvisor must not be null");
        this.methodPeeper = methodPeeper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Pointcut pointcut = new AnnotationMatchingPointcut(null, ExposurePoint.class, true);
        this.advisor = new DefaultPointcutAdvisor(pointcut, new MethodPeeperInterceptor(this.methodPeeper));
    }
}
