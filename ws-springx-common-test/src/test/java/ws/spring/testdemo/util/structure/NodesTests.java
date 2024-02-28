package ws.spring.testdemo.util.structure;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ws.spring.util.structure.*;

import java.util.*;

/**
 * @author WindShadow
 * @version 2023-06-27.
 */

@Slf4j
public class NodesTests {

    private static final Random RANDOM = new Random();

    private static void appendChildren(WritableTreeNode<CustomEntity<String>> node, List<CustomEntity<String>> allEntities, int size, int deep) {

        if (deep <= 0) return;
        CustomEntity<String> parent = node.getIdentity();
        for (int i = 0; i < size; i++) {

            CustomEntity<String> e = new CustomEntity<>(parent.getV() + "-" + i, parent.getV());
            allEntities.add(e);

            WritableTreeNode<CustomEntity<String>> n = node.addChild(e);

            appendChildren(n, allEntities, RANDOM.nextInt(size) + 1, deep - 1);
        }
    }

    private static CustomEntityHolder<String> genEntities(String rootName, int size, int deep) {

        List<CustomEntity<String>> allEntities = new ArrayList<>();
        CustomEntity<String> root = new CustomEntity<>(rootName, null);
        WritableTreeNode<CustomEntity<String>> rootNode = new DefaultTreeNode<>();
        rootNode.setIdentity(root);
        allEntities.add(root);
        appendChildren(rootNode, allEntities, size, deep);

        CustomEntityHolder<String> holder = new CustomEntityHolder<>();
        holder.setAllEntities(allEntities);
        holder.setOriginalNode(rootNode);
        return holder;
    }

    @Test
    void transTest() {

        CustomEntityHolder<String> holder = genEntities("root1", 2, 3);
        List<CustomEntity<String>> allEntities = holder.getAllEntities();

        WritableTreeNode<CustomEntity<String>> node1 = Nodes.transToSingleTree(allEntities);
        WritableTreeNode<CustomEntity<String>> node2 = Nodes.adapterTransToSingleTree(allEntities, CustomEntity::getV, CustomEntity::getPv);
        Assertions.assertEquals(node1, node2);

        TreeNode<CustomEntity<String>> originalNode = holder.getOriginalNode();
        originalNode.sortChildren(Comparator.comparing(CustomEntity::getV));
        node1.sortChildren(Comparator.comparing(CustomEntity::getV));
        node2.sortChildren(Comparator.comparing(CustomEntity::getV));
        Assertions.assertEquals(originalNode, node1);
        Assertions.assertEquals(originalNode, node2);

        List<CustomEntity<String>> otherEntities = genEntities("root2", 2, 3).getAllEntities();
        otherEntities.addAll(allEntities);

        Exception ex;
        ex = Assertions.assertThrows(IllegalArgumentException.class, () -> Nodes.transToSingleTree(otherEntities));
        log.info("IllegalArgumentException: {}", ex.getMessage());

        List<WritableTreeNode<CustomEntity<String>>> treeNodes = Nodes.transformToTrees(otherEntities);
        Assertions.assertSame(2, treeNodes.size());

        List<CustomEntity<String>> duplicateEntities = new ArrayList<>(allEntities);
        duplicateEntities.add(allEntities.get(0));
        ex = Assertions.assertThrows(IllegalStateException.class, () -> Nodes.transToSingleTree(duplicateEntities));
        log.info("IllegalArgumentException: {}", ex.getMessage());
    }

    @Test
    void bfsAndDfsTest() {

        List<CustomEntity<String>> allEntities = genEntities("root", 2, 3).getAllEntities();
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(allEntities);

        CustomEntity<String> entity = allEntities.get(RANDOM.nextInt(allEntities.size()));
        String v = entity.getV();
        CustomEntity<String> bfsRes = Nodes.bfs(tree, e -> v.equals(e.getV()));
        Assertions.assertSame(entity, bfsRes);
        CustomEntity<String> dfsRes = Nodes.dfs(tree, e -> v.equals(e.getV()));
        Assertions.assertSame(entity, dfsRes);
    }

    @Test
    void collectToCollectionTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3).getAllEntities();
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        ArrayList<CustomEntity<String>> collectList = Nodes.collectToCollection(tree, ArrayList::new);
        list.sort(Comparator.comparingInt(Object::hashCode));
        collectList.sort(Comparator.comparingInt(Object::hashCode));
        Assertions.assertEquals(list, collectList);
    }

    @Test
    void collectToListTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3).getAllEntities();
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        List<CustomEntity<String>> entities = Nodes.collectToList(tree);
        list.sort(Comparator.comparingInt(Object::hashCode));
        entities.sort(Comparator.comparingInt(Object::hashCode));
        Assertions.assertEquals(list, entities);
    }

    @Test
    void collectToSetTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3).getAllEntities();
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        Set<CustomEntity<String>> entities = Nodes.collectToSet(tree);
        Assertions.assertEquals(new HashSet<>(list), entities);
    }

    @Test
    void collectToMapTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3).getAllEntities();
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        Map<String, CustomEntity<String>> entityMap = Nodes.collectToMap(tree, CustomEntity::fetchKey);
        list.forEach(e -> Assertions.assertSame(e, entityMap.get(e.fetchKey())));
    }

    @ToString
    private static class CustomEntity<K> implements ForwardNode<K> {

        private final K v;
        private final K pv;

        private String name;

        private CustomEntity(K v, K pv) {
            this.v = v;
            this.pv = pv;
            this.name = "name" + v;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public K getV() {
            return v;
        }

        public K getPv() {
            return pv;
        }

        @Override
        public K fetchKey() {
            return getV();
        }

        @Override
        public K fetchParentKey() {
            return getPv();
        }
    }

    @Data
    private static class CustomEntityHolder<K> {

        private List<CustomEntity<K>> allEntities;
        private TreeNode<CustomEntity<K>> originalNode;
    }
}
