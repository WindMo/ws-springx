/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.structure;

import java.util.List;
import java.util.function.Function;

/**
 * @author WindShadow
 * @version 2023-07-03.
 */

class AdapterForwardNode<E, K> extends BasicTreeNode<E> implements ForwardNode<K> {

    private final Function<? super E, ? extends K> keyMapping;
    private final Function<? super E, ? extends K> parentKeyMapping;

    public AdapterForwardNode(Function<? super E, ? extends K> keyMapping, Function<? super E, ? extends K> parentKeyMapping) {
        this.keyMapping = keyMapping;
        this.parentKeyMapping = parentKeyMapping;
    }

    public AdapterForwardNode(E value, Function<? super E, ? extends K> keyMapping, Function<? super E, ? extends K> parentKeyMapping) {
        super(value);
        this.keyMapping = keyMapping;
        this.parentKeyMapping = parentKeyMapping;
    }

    public AdapterForwardNode(E value, List<WritableTreeNode<E>> children, Function<? super E, ? extends K> keyMapping, Function<? super E, ? extends K> parentKeyMapping) {
        super(value, children);
        this.keyMapping = keyMapping;
        this.parentKeyMapping = parentKeyMapping;
    }

    @Override
    public K fetchParentKey() {

        E value = getIdentity();
        return value == null ? null : parentKeyMapping.apply(value);
    }

    @Override
    public K fetchKey() {

        E value = getIdentity();
        return value == null ? null : keyMapping.apply(value);
    }

    @Override
    protected WritableTreeNode<E> createNode(E value) {
        return null;
    }

    public Function<? super E, ? extends K> getKeyMapping() {
        return keyMapping;
    }

    public Function<? super E, ? extends K> getParentKeyMapping() {
        return parentKeyMapping;
    }
}
