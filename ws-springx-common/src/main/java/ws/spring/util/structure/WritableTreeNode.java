package ws.spring.util.structure;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author WindShadow
 * @version 2023-06-20.
 */

public interface WritableTreeNode<E> extends TreeNode<E> {

    void setIdentity(@Nullable E data);

    void setChildren(@Nullable List<WritableTreeNode<E>> childrenNode);

    WritableTreeNode<E> addChild(@Nullable E value);
}
