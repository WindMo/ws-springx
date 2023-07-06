package ws.spring.util.structure;

import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author WindShadow
 * @version 2023-05-04.
 */

public class Nodes {

    // ~ transform
    // =====================================================================================

    public static <T, K> WritableTreeNode<T> adapterTransToSingleTree(Collection<T> els,
                                                                      Function<? super T, ? extends K> valueMapping,
                                                                      Function<? super T, ? extends K> parentMapping) {

        // new DefaultTreeNode<ForwardNode<K>> 保留泛型信息
        return adapterTransToSingleTree(els, () -> new DefaultTreeNode<ForwardNode<K>>(), valueMapping, parentMapping);
    }

    public static <T, K> WritableTreeNode<T> adapterTransToSingleTree(Collection<T> els,
                                                                      Supplier<WritableTreeNode<ForwardNode<K>>> nodeSupplier,
                                                                      Function<? super T, ? extends K> valueMapping,
                                                                      Function<? super T, ? extends K> parentMapping) {

        Collection<WritableTreeNode<T>> list = adapterTransToTrees(els, nodeSupplier, valueMapping, parentMapping);
        Assert.isTrue(list != null && list.size() == 1, "This forwardNodes can not trans to a single tree node");
        return list.iterator().next();
    }

    public static <T, K> List<WritableTreeNode<T>> adapterTransToTrees(Collection<T> els,
                                                                             Function<? super T, ? extends K> valueMapping,
                                                                             Function<? super T, ? extends K> parentMapping) {
        // new DefaultTreeNode<ForwardNode<K>> 保留泛型信息
        return adapterTransToTrees(els, () -> new DefaultTreeNode<ForwardNode<K>>(), valueMapping, parentMapping);
    }

    public static <T, K> List<WritableTreeNode<T>> adapterTransToTrees(Collection<T> els,
                                                                             Supplier<WritableTreeNode<ForwardNode<K>>> nodeSupplier,
                                                                             Function<? super T, ? extends K> valueMapping,
                                                                             Function<? super T, ? extends K> parentMapping) {

        List<ForwardNode<K>> nodes = els.stream()
                .map(v -> new AdapterForwardNode<>(v, valueMapping, parentMapping))
                .collect(Collectors.toList());
        Collection<WritableTreeNode<ForwardNode<K>>> treeNodes = transformToTrees(nodes, nodeSupplier);

        return treeNodes.stream()
                .map(node -> transform(node,
                        nodeValue -> ((AdapterForwardNode<T, K>) nodeValue).getIdentity(),
                        DefaultTreeNode::new))
                .collect(Collectors.toList());
    }

    public static <K, F extends ForwardNode<K>> WritableTreeNode<F> transToSingleTree(Collection<F> forwardNodes) {

        return transToSingleTree(forwardNodes, DefaultTreeNode::new);
    }

    public static <K, F extends ForwardNode<K>> List<WritableTreeNode<F>> transformToTrees(Collection<F> forwardNodes) {

        return transformToTrees(forwardNodes, DefaultTreeNode::new);
    }

    public static <K, F extends ForwardNode<K>> WritableTreeNode<F> transToSingleTree(Collection<F> forwardNodes, Supplier<WritableTreeNode<F>> nodeSupplier) {

        Collection<WritableTreeNode<F>> list = transformToTrees(forwardNodes, nodeSupplier);
        Assert.isTrue(list != null && list.size() == 1, "This forwardNodes can not trans to a single tree node");
        return list.iterator().next();
    }

    public static <K, F extends ForwardNode<K>> List<WritableTreeNode<F>> transformToTrees(Collection<F> forwardNodes, Supplier<WritableTreeNode<F>> nodeSupplier) {

        return new Transformer<>(forwardNodes, nodeSupplier).trans();
    }

    // ~ transform
    // =====================================================================================

    public static <S, T> WritableTreeNode<T> transform(TreeNode<S> treeNode,
                                                       Function<? super S, ? extends T> mapping,
                                                       Function<? super T, WritableTreeNode<T>> nodeCreator) {

        Assert.notNull(nodeCreator, "The nodeCreator must not be null");
        Assert.notNull(mapping, "The mapping must not be null");

        S value = treeNode.getIdentity();
        WritableTreeNode<T> node = nodeCreator.apply(mapping.apply(value));
        if (treeNode.hasChildren()) {
            Collection<WritableTreeNode<T>> children = treeNode.getChildren()
                    .stream()
                    .map(n -> transform(n, mapping, nodeCreator))
                    .collect(Collectors.toList());
            node.setChildren(children);
        }
        return node;
    }

    // ~ collect
    // =====================================================================================

    public static <T, K> Map<K, T> collectToMap(TreeNode<T> treeNode,
                                                boolean deepFist,
                                                Predicate<T> terminator,
                                                Function<? super T, ? extends K> keyConverter) {

        return collectToMap(treeNode, deepFist, HashMap::new, terminator, keyConverter, Function.identity());
    }

    public static <T, K> Map<K, T> collectToMap(TreeNode<T> treeNode,
                                                boolean deepFist,
                                                Supplier<Map<K, T>> supplier,
                                                Predicate<T> terminator,
                                                Function<? super T, ? extends K> keyConverter) {

        return collectToMap(treeNode, deepFist, supplier, terminator, keyConverter, Function.identity());
    }

    public static <T, K, V> Map<K, V> collectToMap(TreeNode<T> treeNode,
                                                   boolean deepFist,
                                                   Predicate<T> terminator,
                                                   Function<? super T, ? extends K> keyConverter,
                                                   Function<? super T, ? extends V> valueConverter) {

        return collectToMap(treeNode, deepFist, HashMap::new, terminator, keyConverter, valueConverter);
    }

    public static <T, K, V> Map<K, V> collectToMap(TreeNode<T> treeNode,
                                                   boolean deepFist,
                                                   Supplier<Map<K, V>> supplier,
                                                   Predicate<T> terminator,
                                                   Function<? super T, ? extends K> keyConverter,
                                                   Function<? super T, ? extends V> valueConverter) {

        Map<K, V> map = Objects.requireNonNull(supplier, "The supplier must not be null").get();
        iterate(treeNode, deepFist, terminator,
                v -> map.computeIfAbsent(keyConverter.apply(v), k -> valueConverter.apply(v)));
        return map;
    }

    public static <T> Set<T> collectToSet(TreeNode<T> treeNode,
                                          boolean deepFist,
                                          Predicate<T> terminator) {

        return collectToCollection(treeNode, deepFist, HashSet::new, terminator, Function.identity());
    }

    public static <T, E> Set<E> collectToSet(TreeNode<T> treeNode,
                                             boolean deepFist,
                                             Predicate<T> terminator,
                                             Function<? super T, ? extends E> converter) {

        return collectToCollection(treeNode, deepFist, HashSet::new, terminator, converter);
    }

    public static <T> List<T> collectToList(TreeNode<T> treeNode,
                                            boolean deepFist,
                                            Predicate<T> terminator) {

        return collectToCollection(treeNode, deepFist, ArrayList::new, terminator, Function.identity());
    }

    public static <T, E> List<E> collectToList(TreeNode<T> treeNode,
                                               boolean deepFist,
                                               Predicate<T> terminator,
                                               Function<? super T, ? extends E> converter) {

        return collectToCollection(treeNode, deepFist, ArrayList::new, terminator, converter);
    }

    public static <T, E, C extends Collection<E>> C collectToCollection(TreeNode<T> treeNode,
                                                                        boolean deepFist,
                                                                        Supplier<C> supplier,
                                                                        Predicate<T> terminator,
                                                                        Function<? super T, ? extends E> converter) {

        Assert.notNull(supplier, "The supplier must not be null");
        C collection = supplier.get();
        iterate(treeNode, deepFist, terminator, v -> collection.add(converter.apply(v)));
        return collection;
    }

    // ~ search
    // =====================================================================================

    public static <T> T bfs(TreeNode<T> treeNode, Predicate<T> comparator) {
        return search(treeNode, false, comparator);
    }

    public static <T> T dfs(TreeNode<T> treeNode, Predicate<T> comparator) {
        return search(treeNode, true, comparator);
    }

    private static <T> T search(TreeNode<T> treeNode, boolean deepFist, Predicate<T> comparator) {

        Object[] holder = new Object[1];
        iterate(treeNode, deepFist, comparator.negate(), v -> holder[0] = v);
        return holder[0] == null ? null : (T) holder[0];
    }

    // ~ iterate
    // =====================================================================================

    public static <T> void iterate(TreeNode<T> treeNode, boolean deepFirst, Predicate<T> terminator, Consumer<T> consumer) {

        Assert.notNull(treeNode, "The node must not be null");
        Assert.notNull(terminator, "The terminator must not be null");
        Assert.notNull(consumer, "The consumer must not be null");
        if (deepFirst) {
            deepFistIterate(treeNode, terminator, consumer);
        } else {
            breadthFistIterate(treeNode, terminator, consumer);
        }
    }

    private static <T> void breadthFistIterate(TreeNode<T> treeNode, Predicate<T> terminator, Consumer<T> consumer) {

        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(treeNode);
        while (!queue.isEmpty()) {

            TreeNode<T> n = queue.poll();
            T value = treeNode.getIdentity();
            if (terminator.test(value)) {

                consumer.accept(value);
                if (treeNode.hasChildren()) {
                    queue.addAll(Objects.requireNonNull(n.getChildren(), "The node has children but it is null"));
                }
            }
        }
    }

    private static <T> void deepFistIterate(TreeNode<T> treeNode, Predicate<T> terminator, Consumer<T> consumer) {

        T value = treeNode.getIdentity();
        if (terminator.test(value)) {

            consumer.accept(value);
            if (treeNode.hasChildren()) {
                Collection<? extends TreeNode<T>> children = treeNode.getChildren();
                Assert.notNull(children, "The node has children but it is null");
                children.forEach(n -> deepFistIterate(n, terminator, consumer));
            }
        }
    }

    // ~ internal class
    // =====================================================================================

    private static class Transformer<K, F extends ForwardNode<K>> {

        private final Collection<F> forwardNodes;
        private final Supplier<WritableTreeNode<F>> nodeSupplier;
        private Map<K, F> nodeMapping;
        private Map<K, WritableTreeNode<F>> records;
        private List<WritableTreeNode<F>> result;

        public Transformer(Collection<F> forwardNodes, Supplier<WritableTreeNode<F>> nodeSupplier) {

            Assert.notNull(forwardNodes, "The forwardNodes must not be null");
            Assert.notNull(nodeSupplier, "The nodeSupplier must not be null");
            this.forwardNodes = forwardNodes;
            this.nodeSupplier = nodeSupplier;
        }

        private void resize() {

            nodeMapping = forwardNodes
                    .stream()
                    .collect(Collectors.toMap(ForwardNode::fetchKey, Function.identity()));
            records = new HashMap<>();
            result = new ArrayList<>();
        }

        private WritableTreeNode<F> findNode(F fn) {

            K key = fn.fetchKey();
            WritableTreeNode<F> node = records.get(key);
            if (node == null) {

                F parentFn = nodeMapping.get(fn.fetchParentKey());
                if (parentFn != null) {

                    WritableTreeNode<F> pn = findNode(parentFn);
                    if (pn != null) {
                        node = pn.addChild(fn);
                    }
                } else {

                    node = nodeSupplier.get();
                    node.setIdentity(fn);
                    result.add(node);
                }
                if (node != null) {
                    records.put(key, node);
                }
            }
            return node;
        }

        public List<WritableTreeNode<F>> trans() {

            if (forwardNodes.isEmpty()) {
                return Collections.emptyList();
            }
            resize();
            forwardNodes.forEach(this::findNode);
            return result;
        }
    }
}
