package ws.spring.util.structure;

import org.springframework.lang.Nullable;

import java.util.Collection;

/**
 * @author WindShadow
 * @version 2023-05-04.
 */

public interface TreeNode<E> extends Node<E> {

    default boolean hasChildren() {

        Collection<? extends TreeNode<E>> children = getChildren();
        return children != null && !children.isEmpty();
    }

    @Nullable
    Collection<? extends TreeNode<E>> getChildren();
}
