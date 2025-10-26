/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.aop.support;

import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ws.spring.aop.MethodPeeper;
import ws.spring.aop.ReturnValuePeeper;
import ws.spring.aop.annotation.ExposurePoint;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WindShadow
 * @version 2022-02-12.
 */

public class MethodPeeperComposite<T> implements MethodPeeper<T> {

    private final List<OrderedMethodPeeper<T>> methodPeepers = new ArrayList<>();

    public MethodPeeperComposite<T> addMethodPeeper(MethodPeeper<T> methodPeeper) {

        return addSortedMethodPeeper(new OrderedMethodPeeper<>(methodPeeper));
    }

    public MethodPeeperComposite<T> addMethodPeeper(MethodPeeper<T> methodPeeper, int order) {

        return addSortedMethodPeeper(new OrderedMethodPeeper<>(order, methodPeeper));
    }

    public MethodPeeperComposite<T> addMethodPeepers(@Nullable MethodPeeper<T>... methodPeepers) {

        if (methodPeepers != null) {

            return addMethodPeepers(Arrays.asList(methodPeepers));
        }
        return this;
    }

    public MethodPeeperComposite<T> addMethodPeepers(@Nullable List<MethodPeeper<T>> methodPeepers) {

        if (methodPeepers != null) {
            List<OrderedMethodPeeper<T>> methodPeeperList = methodPeepers.stream()
                    .map(OrderedMethodPeeper::new)
                    .collect(Collectors.toList());
            return addSortedMethodPeepers(methodPeeperList);
        }
        return this;
    }

    private MethodPeeperComposite<T> addSortedMethodPeeper(OrderedMethodPeeper<T> orderedMethodPeeper) {

        if (orderedMethodPeeper != null) {
            this.methodPeepers.add(orderedMethodPeeper);
            sortMethodPeepers();
        }
        return this;
    }

    private MethodPeeperComposite<T> addSortedMethodPeepers(@Nullable List<OrderedMethodPeeper<T>> orderedMethodPeepers) {

        if (orderedMethodPeepers != null) {
            this.methodPeepers.addAll(orderedMethodPeepers);
            sortMethodPeepers();
        }
        return this;
    }

    private void sortMethodPeepers() {

        this.methodPeepers.sort(Comparator.comparing(OrderedMethodPeeper::getOrder));
    }

    public List<MethodPeeper<T>> getMethodPeepers() {
        return Collections.unmodifiableList(this.methodPeepers);
    }

    @Override
    public ReturnValuePeeper<T> peekArguments(ExposurePoint exposurePoint, Class<?> clazz, Method method, Object... args) {

        List<ReturnValuePeeper<T>> returnValuePeepers = methodPeepers
                .stream()
                .map(methodPeeper -> methodPeeper.peekArguments(exposurePoint, clazz, method, args))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return (returnValue, ex) -> returnValuePeepers.forEach(returnValuePeeper -> returnValuePeeper.peekReturnValue(returnValue, ex));
    }

    protected static class OrderedMethodPeeper<T> implements MethodPeeper<T>, Ordered {

        private int order = Ordered.LOWEST_PRECEDENCE;
        private MethodPeeper<T> delegate;

        public OrderedMethodPeeper() {
        }

        public OrderedMethodPeeper(MethodPeeper<T> delegate) {
            this.delegate = delegate;
        }

        public OrderedMethodPeeper(int order, MethodPeeper<T> delegate) {
            this.order = order;
            this.delegate = delegate;
        }

        public MethodPeeper<T> getDelegate() {
            return delegate;
        }

        public void setDelegate(MethodPeeper<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        @Override
        public ReturnValuePeeper<T> peekArguments(ExposurePoint exposurePoint, Class<?> clazz, Method method, Object... args) {

            Assert.state(delegate != null, "The delegate must not be null");
            return delegate.peekArguments(exposurePoint, clazz, method, args);
        }
    }
}
