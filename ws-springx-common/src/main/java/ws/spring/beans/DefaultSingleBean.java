/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.beans;

/**
 * @author WindShadow
 * @version 2023-07-21.
 */

public class DefaultSingleBean<T> implements SingleBean<T> {

    private T value;

    public DefaultSingleBean() {
    }

    public DefaultSingleBean(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DefaultSingleBean{" +
                "value=" + value +
                '}';
    }
}
