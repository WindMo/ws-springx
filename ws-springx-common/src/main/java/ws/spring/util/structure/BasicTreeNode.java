package ws.spring.util.structure;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2023-05-04.
 */

public abstract class BasicTreeNode<E> implements WritableTreeNode<E> {

    @Nullable
    private E identity;

    @Nullable
    private Collection<WritableTreeNode<E>> children;

    public BasicTreeNode() {
    }

    public BasicTreeNode(@Nullable E identity) {
        this.identity = identity;
    }

    public BasicTreeNode(@Nullable E identity, @Nullable Collection<WritableTreeNode<E>> children) {
        this.identity = identity;
        this.children = children;
    }

    protected abstract WritableTreeNode<E> createNode(E value);

    private Collection<WritableTreeNode<E>> fetchChildren() {

        Collection<WritableTreeNode<E>> c = getChildren();
        if (c == null) {
            c = children = new LinkedList<>();
        }
        return c;
    }

    private WritableTreeNode<E> doAddChild(E value) {

        WritableTreeNode<E> node = createNode(value);
        fetchChildren().add(node);
        return node;
    }

    @Override
    public E getIdentity() {
        return this.identity;
    }

    @Override
    public void setIdentity(@Nullable E identity) {
        this.identity = identity;
    }

    @Override
    @Nullable
    public Collection<WritableTreeNode<E>> getChildren() {
        return children;
    }

    @Override
    public void setChildren(@Nullable Collection<WritableTreeNode<E>> childrenNode) {
        this.children = childrenNode;
    }

    @Override
    public WritableTreeNode<E> addChild(E value) {

        return doAddChild(value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "value=" + identity +
                ", children=" + children +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicTreeNode<?> that = (BasicTreeNode<?>) o;
        return Objects.equals(identity, that.identity) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, children);
    }
}
