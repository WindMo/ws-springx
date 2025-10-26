/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.structure;

import org.springframework.lang.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author WindShadow
 * @version 2023-05-04.
 */

public interface TreeNode<E> extends Node<E> {

    default boolean hasChildren() {

        List<? extends TreeNode<E>> children = getChildren();
        return children != null && !children.isEmpty();
    }

    @Nullable
    List<? extends TreeNode<E>> getChildren();

    default void sortChildren(Comparator<E> comparator) {
        Optional.ofNullable(getChildren())
                .ifPresent(children -> {

                    children.sort((c1, c2) -> comparator.compare(c1.getIdentity(), c2.getIdentity()));
                    children.forEach(child -> child.sortChildren(comparator));
                });
    }
}
