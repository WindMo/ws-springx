package ws.spring.util.structure;

import org.springframework.lang.Nullable;

/**
 * @author WindShadow
 * @version 2023-06-19.
 */

public interface ForwardNode<K> {

    K fetchKey();

    @Nullable
    K fetchParentKey();
}
