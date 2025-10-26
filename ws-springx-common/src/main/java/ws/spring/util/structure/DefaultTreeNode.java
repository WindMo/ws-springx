/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.structure;

import java.util.List;

/**
 * @author WindShadow
 * @version 2023-06-19.
 */

public class DefaultTreeNode<E> extends BasicTreeNode<E> {

    public DefaultTreeNode() {
    }

    public DefaultTreeNode(E identity) {
        super(identity);
    }

    public DefaultTreeNode(E value, List<WritableTreeNode<E>> children) {
        super(value, children);
    }

    @Override
    protected WritableTreeNode<E> createNode(E value) {
        return new DefaultTreeNode<>(value);
    }
}
